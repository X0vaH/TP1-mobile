package com.example.tp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1.data.Entrainement
import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EntrainementsViewModel(
    private val repository: IEntrainementRepository = FakeEntrainementRepository()
): ViewModel() {
    private val _uiState = MutableStateFlow(EntrainementsUiState())

    val uiState: StateFlow<EntrainementsUiState> = _uiState.asStateFlow()

    init {
        chargerEntrainements()
    }

    fun selectionneEntrainement(entrainement: Entrainement) {

        _uiState.update {
            it.copy(
                entrainementSelectionne = entrainement
            )
        }
    }

    private fun chargerEntrainements() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            try {
                val entrainements = repository.chargerEntrainements()
                _uiState.update {
                    it.copy(isLoading = false, entrainements = entrainements)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.toString(), isLoading = false)
                }
            }
        }
    }


}