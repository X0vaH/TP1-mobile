package com.example.tp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEntrainementsViewModel(
    private val repository: IEntrainementRepository = FakeEntrainementRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEntrainementsUiState())

    val uiState = _uiState.asStateFlow()

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

        var formulaireValide = true

        if (state.titre.length < 3) {
            _uiState.update {
                it.copy(erreurTitre = "Le titre doit contenire au moins 3 caractères")
            }
            formulaireValide = false
        }

        if (state.lieu.isBlank()) {
            _uiState.update {
                it.copy(erreurLieu = "Le lieu est obligatoire")
            }
            formulaireValide = false
        }

        if (state.intensite !in 1..10) {
            _uiState.update {
                it.copy(erreurIntensite = "L'intensité est obligatoire")
            }
            formulaireValide = false
        }
        if (state.activite == null) {
            _uiState.update {
                it.copy(erreurActivite = "Le type d'activité est obligatoire")
            }
            formulaireValide = false
        }
        if (!formulaireValide) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, erreur = null) }

            try {
                repository.ajouter(
                    Entrainement(
                        id = -1,
                        titre = state.titre,
                        activite = state.activite!!,
                        lieu = state.lieu,
                        exterieur = state.exterieur,
                        intensite = state.intensite,
                        notes = state.notes
                    )
                )

                onSuccess()

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