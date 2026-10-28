package com.example.tp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1.BuildConfig
import com.example.tp1.R
import com.example.tp1.data.Entrainement
import com.example.tp1.data.ExerciceSeance
import com.example.tp1.data.TypeActivite
import com.example.tp1.network.ExerciceDTO
import com.example.tp1.repository.IEntrainementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

class CreateEntrainementsViewModel(
    private val repository: IEntrainementRepository,
    private val entrainementId: Int? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEntrainementsUiState(isLoading = entrainementId != null, estModification = entrainementId != null))

    val uiState = _uiState.asStateFlow()

    private var entrainementOriginal: Entrainement? = null
    private var rechercheJob: Job? = null

    init {
        if (entrainementId != null) {
            viewModelScope.launch {
                try {
                    val seance = repository.obtenirAvecExercices(entrainementId)
                        ?: throw IllegalArgumentException("Cette séance n'existe plus.")
                    entrainementOriginal = seance
                    _uiState.value = CreateEntrainementsUiState(
                        titre = seance.titre,
                        activite = seance.activite,
                        lieu = seance.lieu,
                        exterieur = seance.exterieur,
                        intensite = seance.intensite,
                        notes = seance.notes,
                        exercices = seance.exercices.map { it.toForm() },
                        estModification = true
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, erreur = e.message ?: "Impossible de charger la séance.") }
                }
            }
        }
    }
    fun modifierTitre(titre: String) {
        _uiState.update {
            it.copy(
                titre = titre,
                erreurTitre = null
            )
        }
    }

    fun modifierActivite(activite: TypeActivite) {
        _uiState.update {
            it.copy(
                activite = activite,
                erreurActivite = null
            )
        }
    }

    fun modifierLieu(lieu:String) {
        _uiState.update {
            it.copy(
                lieu = lieu,
                erreurLieu = null
            )
        }
    }

    fun modifierExterieur(exterieur:Boolean) {
        _uiState.update {
            it.copy(
                exterieur = exterieur,
                erreurLieu = null
            )
        }
    }

    fun modifierIntensite(intensite:Int) {
        _uiState.update {
            it.copy(
                intensite = intensite,
                erreurIntensite = null
            )
        }
    }

    fun modifierNotes(notes:String) {
        _uiState.update {
            it.copy(
                notes = notes,
                erreurNotes = null
            )
        }
    }

    fun ouvrirRecherche() {
        rechercheJob?.cancel()
        _uiState.update {
            it.copy(recherche = RechercheExerciceUiState(afficherDialogue = true))
        }
    }

    fun fermerRecherche() {
        rechercheJob?.cancel()
        _uiState.update {
            it.copy(recherche = RechercheExerciceUiState())
        }
    }

    fun modifierQueryRecherche(query: String) {
        _uiState.update {
            it.copy(recherche = it.recherche.copy(query = query, erreur = null))
        }
    }

    fun rechercherExercices() {
        val recherche = _uiState.value.recherche
        val query = recherche.query.trim()
        if (query.isEmpty() || recherche.isSearching) return

        _uiState.update {
            it.copy(
                recherche = it.recherche.copy(
                    isSearching = true,
                    rechercheEffectuee = false,
                    erreur = null,
                    resultats = emptyList()
                )
            )
        }
        rechercheJob = viewModelScope.launch {
            repository.rechercherExercicesDepuisApi(query, BuildConfig.API_KEY)
                .onSuccess { exercices ->
                    _uiState.update {
                        it.copy(
                            recherche = it.recherche.copy(
                                isSearching = false,
                                rechercheEffectuee = true,
                                resultats = exercices
                            )
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            recherche = it.recherche.copy(
                                isSearching = false,
                                erreur = e.message ?: "Erreur lors de la recherche."
                            )
                        )
                    }
                }
        }
    }

    fun ajouterExercice(exercice: ExerciceDTO) {
        rechercheJob?.cancel()
        _uiState.update {
            it.copy(
                exercices = it.exercices + exercice.toForm(),
                recherche = RechercheExerciceUiState()
            )
        }
    }

    fun retirerExercice(index: Int) {
        _uiState.update {
            it.copy(exercices = it.exercices.filterIndexed { i, _ -> i != index })
        }
    }

    fun modifierSeries(index: Int, series: String) {
        modifierExercice(index) { it.copy(series = series, erreurSeries = null) }
    }

    fun modifierRepetitions(index: Int, repetitions: String) {
        modifierExercice(index) { it.copy(repetitions = repetitions, erreurRepetitions = null) }
    }

    fun modifierCharge(index: Int, charge: String) {
        modifierExercice(index) { it.copy(charge = charge, erreurCharge = null) }
    }

    private fun modifierExercice(index: Int, modification: (ExerciceFormUiState) -> ExerciceFormUiState) {
        _uiState.update {
            it.copy(
                exercices = it.exercices.mapIndexed { i, exercice ->
                    if (i == index) modification(exercice) else exercice
                }
            )
        }
    }

    fun creerEntrainement(onSuccess: ()-> Unit) {
        val state = _uiState.value

        if (state.isSaving || state.isLoading || (entrainementId != null && entrainementOriginal == null)) return

        var formulaireValide = true

        if (state.titre.trim().length < 3) {
            _uiState.update {
                it.copy(erreurTitre = R.string.error_title)
            }
            formulaireValide = false
        }

        if (state.lieu.isBlank()) {
            _uiState.update {
                it.copy(erreurLieu = R.string.erreur_location)
            }
            formulaireValide = false
        }

        if (state.intensite !in 1..10) {
            _uiState.update {
                it.copy(erreurIntensite = R.string.error_intensity)
            }
            formulaireValide = false
        }
        if (state.activite == null) {
            _uiState.update {
                it.copy(erreurActivite = R.string.error_activity_type)
            }
            formulaireValide = false
        }
        // Les exercices ne concernent que les séances de musculation
        val exercices = if (state.activite == TypeActivite.MUSCULATION) state.exercices else emptyList()
        val exercicesValides = exercices.map { it.valider() }
        if (exercicesValides.any { it.erreurSeries != null || it.erreurRepetitions != null || it.erreurCharge != null }) {
            _uiState.update { it.copy(exercices = exercicesValides) }
            formulaireValide = false
        }

        if (!formulaireValide) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, erreur = null) }

            try {
                val seance = Entrainement(
                    id = entrainementId ?: -1,
                    titre = state.titre.trim(),
                    activite = state.activite!!,
                    lieu = state.lieu.trim(),
                    exterieur = state.exterieur,
                    intensite = state.intensite,
                    notes = state.notes,
                    exercices = exercices.map { it.toModel() }
                )
                if (entrainementId == null) {
                    repository.ajouter(seance)
                } else {
                    repository.modifier(seance)
                }
                _uiState.update { it.copy(isSaving = false) }
                onSuccess()

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        erreur = e.message
                    )
                }
            }
        }
    }
}

private fun ExerciceSeance.toForm(): ExerciceFormUiState {
    return ExerciceFormUiState(
        idExterne = idExterne,
        nom = nom,
        cible = cible,
        equipement = equipement,
        series = series.toString(),
        repetitions = repetitions.toString(),
        charge = charge.toString()
    )
}

private fun ExerciceDTO.toForm(): ExerciceFormUiState {
    return ExerciceFormUiState(
        idExterne = id,
        nom = name,
        cible = target,
        equipement = equipment
    )
}

private fun ExerciceFormUiState.valider(): ExerciceFormUiState {
    return copy(
        erreurSeries = if ((series.toIntOrNull() ?: 0) < 1) "Le nombre de séries doit être un entier d'au moins 1" else null,
        erreurRepetitions = if ((repetitions.toIntOrNull() ?: 0) < 1) "Le nombre de répétitions doit être un entier d'au moins 1" else null,
        erreurCharge = if ((charge.replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() } ?: -1.0) < 0.0) "La charge doit être un nombre positif ou nul" else null
    )
}

private fun ExerciceFormUiState.toModel(): ExerciceSeance {
    return ExerciceSeance(
        idExterne = idExterne,
        nom = nom,
        cible = cible,
        equipement = equipement,
        series = series.toInt(),
        repetitions = repetitions.toInt(),
        charge = charge.replace(',', '.').toDouble()
    )
}
