package com.example.tp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1.data.TypeActivite
import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EntrainementsViewModel(
    private val repository: IEntrainementRepository = FakeEntrainementRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(EntrainementsUiState())
    private val _idSelectionne = MutableStateFlow<Int?>(null)
    private val _afficherDialogueSuppression = MutableStateFlow(false)
    val uiState: StateFlow<EntrainementsUiState> = _uiState.asStateFlow()


    init {
        chargerEntrainements()
    }

    val accueilUiState: StateFlow<AccueilUiState> = _uiState
        .map { etat ->
            AccueilUiState(
                totalSeances = etat.entrainements.size,
                seancesCompletees = etat.entrainements.count { it.estComplete },
                prochaineSeance = etat.entrainements.firstOrNull { !it.estComplete },
                isLoading = etat.isLoading,
                errorMessage = etat.errorMessage
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AccueilUiState()
        )


    val detailUiState: StateFlow<EntrainementDetailUiState> = combine(
        _uiState, _idSelectionne, _afficherDialogueSuppression
    ) { etat, id, dialogue ->
        EntrainementDetailUiState(
            entrainement = etat.entrainements.find { it.id == id },
            isLoading = etat.isLoading,
            afficherDialogueSuppression = dialogue
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EntrainementDetailUiState()
    )

    fun onRechercheChange(query: String) {
        _uiState.update { it.copy(rechercheQuery = query) }
    }

    fun onFiltreFavorisChange(favoris: Boolean) {
        _uiState.update { it.copy(filtreFavoris = favoris) }
    }

    fun onTypeActiviteChange(type: TypeActivite?) {
        _uiState.update { it.copy(typeActiviteSelectionne = type) }
    }

    fun basculerComplete(id: Int) {
        viewModelScope.launch { repository.basculerComplete(id) }
    }

    fun basculerFavori(id: Int) {
        viewModelScope.launch { repository.basculerFavori(id) }
    }

    fun supprimerEntrainement(id: Int) {
        viewModelScope.launch { repository.supprimer(id) }
    }

    fun chargerDetail(id: Int) {
        _idSelectionne.value = id
    }

    fun afficherDialogueSuppression() {
        _afficherDialogueSuppression.value = true
    }

    fun masquerDialogueSuppression() {
        _afficherDialogueSuppression.value = false
    }

    fun confirmerSuppression() {
        val id = _idSelectionne.value ?: return
        _afficherDialogueSuppression.value = false
        supprimerEntrainement(id)
    }

    private fun chargerEntrainements() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val liste = repository.chargerEntrainements()
                _uiState.update { it.copy(isLoading = false, entrainements = liste) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.toString(), isLoading = false) }
                return@launch
            }
            repository.observerEntrainements().collect { liste ->
                _uiState.update { it.copy(entrainements = liste) }
            }
        }
    }
}