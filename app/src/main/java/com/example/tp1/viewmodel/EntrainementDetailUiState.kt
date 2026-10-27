package com.example.tp1.viewmodel

import com.example.tp1.data.Entrainement

data class EntrainementDetailUiState(
    val entrainement: Entrainement? = null,
    val isLoading: Boolean = true,
    val afficherDialogueSuppression: Boolean = false,
    val consignes: ConsignesUiState = ConsignesUiState()
)

// Consignes en français des exercices, chargées depuis l'API MoveBase (clé : id MoveBase de l'exercice)
data class ConsignesUiState(
    val consignes: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val erreur: String? = null
)
