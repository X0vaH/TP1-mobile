package com.example.tp1.viewmodel


import androidx.compose.ui.graphics.Path.Companion.combine
import androidx.compose.ui.text.style.TextDecoration.Companion.combine
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
import kotlinx.coroutines.flow.catch
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
    private val _errorMessage = MutableStateFlow<String?>(null)
    val uiState: StateFlow<EntrainementsUiState> = combine(
        entrainementsFlow,
        _rechercheQuery,
        _filtreFavoris,
        _typeActivite,
        _errorMessage,
        repository.observerEntrainements().catch { e ->
            _errorMessage.value = e.message
            emit(emptyList())
        }
    ){ liste, recherche, favoris, type, erreur ->
        val filtres = liste
            .filter { it.titre.contains(recherche, ignoreCase = true) }
            .filter { !favoris || it.estFavori }
            .filter { type == null || it.type == type }

        EntrainementsUiState(
            entrainements = filtres,
            isLoading = false,
            errorMessage = erreur,
            rechercheQuery = recherche,
            filtreFavoris = favoris,
            typeActiviteSelectionne = type
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EntrainementsUiState(isLoading = true)
    )

    // Écran accueil : totaux sur la liste NON filtrée
    val accueilUiState: StateFlow<AccueilUiState> = combine(
        entrainementsFlow, _errorMessage
    ) { liste, erreur ->
        AccueilUiState(
            totalSeances = liste.size,
            seancesCompletees = liste.count { it.estComplete },
            prochaineSeance = liste.firstOrNull { !it.estComplete },
            isLoading = false,
            errorMessage = erreur
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AccueilUiState(isLoading = true)
    )

    // Écran détail
    val detailUiState: StateFlow<EntrainementDetailUiState> = combine(
        entrainementsFlow, _idSelectionne, _afficherDialogueSuppression
    ) { liste, id, dialogue ->
        EntrainementDetailUiState(
            entrainement = liste.find { it.id == id },
            isLoading = false,
            afficherDialogueSuppression = dialogue
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EntrainementDetailUiState(isLoading = true)
    )


    init {
        chargerEntrainements()
    }



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