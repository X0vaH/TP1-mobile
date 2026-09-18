package com.example.tp1.viewmodel

import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateEntrainementsViewModel(
    private val repository: IEntrainementRepository = FakeEntrainementRepository()
) {

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
    fun modifierActivite(activite:String) {
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
        // TODO
    }
}