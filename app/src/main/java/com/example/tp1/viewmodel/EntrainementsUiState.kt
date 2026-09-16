package com.example.tp1.viewmodel

import com.example.tp1.data.Entrainement

data class EntrainementsUiState(
    val entrainements: List<Entrainement> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val entrainementSelectionne: Entrainement? = null
)