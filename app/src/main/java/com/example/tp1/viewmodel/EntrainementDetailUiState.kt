package com.example.tp1.viewmodel

import com.example.tp1.data.Entrainement

data class EntrainementDetailUiState(
    val entrainement: Entrainement? = null,
    val isLoading: Boolean = true,
    val afficherDialogueSuppression: Boolean = false
)