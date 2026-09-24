package com.example.tp1.viewmodel

import com.example.tp1.data.Entrainement

data class AccueilUiState(
    val totalSeances: Int = 0,
    val seancesCompletees: Int = 0,
    val prochaineSeance: Entrainement? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val progression: Float
        get() = if (totalSeances == 0) 0f else seancesCompletees.toFloat() / totalSeances
}