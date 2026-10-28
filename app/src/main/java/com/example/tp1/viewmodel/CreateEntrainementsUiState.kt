package com.example.tp1.viewmodel

import androidx.annotation.StringRes
import com.example.tp1.data.TypeActivite
import com.example.tp1.network.ExerciceDTO

data class CreateEntrainementsUiState (
    val titre: String = "",
    val activite: TypeActivite? = null,
    val lieu: String ="",
    val exterieur: Boolean = true,
    val intensite: Int = 0,
    val notes: String = "",
    val exercices: List<ExerciceFormUiState> = emptyList(),
    val recherche: RechercheExerciceUiState = RechercheExerciceUiState(),
    @StringRes val erreurTitre: Int? = null,
    @StringRes val erreurActivite: Int? = null,
    @StringRes val erreurLieu: Int? = null,
    val erreurExterieur: String? = null,
    @StringRes val erreurIntensite: Int? = null,
    val erreurNotes: String? = null,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val estModification: Boolean = false,
    val erreur: String? = null

)

// Champs numériques en String (saisie texte)
data class ExerciceFormUiState(
    val idExterne: String,
    val nom: String,
    val cible: String,
    val equipement: String,
    val series: String = "3",
    val repetitions: String = "10",
    val charge: String = "0",
    val erreurSeries: String? = null,
    val erreurRepetitions: String? = null,
    val erreurCharge: String? = null
)

data class RechercheExerciceUiState(
    val afficherDialogue: Boolean = false,
    val query: String = "",
    val isSearching: Boolean = false,
    val rechercheEffectuee: Boolean = false,
    val erreur: String? = null,
    val resultats: List<ExerciceDTO> = emptyList()
)
