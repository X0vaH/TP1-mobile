package com.example.tp1.viewmodel

data class CreateEntrainementsUiState (
    val titre: String = "",
    val activite: String = "",
    val lieu: String ="",
    val exterieur: Boolean = true,
    val intensite: Int = 0,
    val notes: String = "",
    val erreurTitre: String? = null,
    val erreurActivite: String? = null,
    val erreurLieu: String? = null,
    val erreurExterieur: String? = null,
    val erreurIntensite: String? = null,
    val erreurNotes: String? = null,
    val isSaving: Boolean = false,
    val erreur: String? = null

)