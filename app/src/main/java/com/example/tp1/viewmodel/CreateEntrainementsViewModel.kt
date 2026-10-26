package com.example.tp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1.R
import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException

class CreateEntrainementsViewModel(
    private val repository: IEntrainementRepository = FakeEntrainementRepository(),
    private val entrainementId: Int? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEntrainementsUiState(isLoading = entrainementId != null, estModification = entrainementId != null))

    val uiState = _uiState.asStateFlow()

    private var entrainementOriginal: Entrainement? = null

    init {
        if (entrainementId != null) {
            viewModelScope.launch {
                try {
                    val seance = repository.obtenirParId(entrainementId)
                        ?: throw IllegalArgumentException("Cette séance n'existe plus.")
                    entrainementOriginal = seance
                    _uiState.value = CreateEntrainementsUiState(
                        titre = seance.titre,
                        activite = seance.activite,
                        lieu = seance.lieu,
                        exterieur = seance.exterieur,
                        intensite = seance.intensite,
                        notes = seance.notes,
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

    fun creerEntrainement(onSuccess: ()-> Unit) {
        val state = _uiState.value

        if (state.isSaving || state.isLoading || (entrainementId != null && entrainementOriginal == null)) return

        var formulaireValide = true

        if (state.titre.trim().length < 3) {
            _uiState.update {
                it.copy(erreurTitre = R.string.error_title.toString())
            }
            formulaireValide = false
        }

        if (state.lieu.isBlank()) {
            _uiState.update {
                it.copy(erreurLieu = R.string.erreur_location.toString())
            }
            formulaireValide = false
        }

        if (state.intensite !in 1..10) {
            _uiState.update {
                it.copy(erreurIntensite = R.string.error_intensity.toString())
            }
            formulaireValide = false
        }
        if (state.activite == null) {
            _uiState.update {
                it.copy(erreurActivite = R.string.error_activity_type.toString())
            }
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
                    notes = state.notes
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