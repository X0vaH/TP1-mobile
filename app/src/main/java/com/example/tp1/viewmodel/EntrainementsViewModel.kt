package com.example.tp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1.BuildConfig
import com.example.tp1.network.ExerciceDTO
import kotlinx.coroutines.flow.update
import com.example.tp1.data.TypeActivite
import kotlinx.coroutines.CancellationException
import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EntrainementsViewModel(
    private val repository: IEntrainementRepository = FakeEntrainementRepository()
) : ViewModel() {

    // Local UI state (not persisted)
    private val _rechercheQuery = MutableStateFlow("")
    private val _filtreFavoris = MutableStateFlow(false)
    private val _typeActivite = MutableStateFlow<TypeActivite?>(null)
    private val _idSelectionne = MutableStateFlow<Int?>(null)
    private val _afficherDialogueSuppression = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val rechercheApiState = MutableStateFlow(RechercheMoveBaseUiState())
    private val filtresState = combine(_rechercheQuery, _filtreFavoris, _typeActivite) { query, favoris, type ->
        Triple(query, favoris, type)
    }

    // Room source, shared by the three screens
    private val entrainementsFlow = repository.observerEntrainements()
        .catch { e ->
            _errorMessage.value = e.message
            emit(emptyList())
        }

    // List screen
    val uiState: StateFlow<EntrainementsUiState> = combine(
        entrainementsFlow,
        filtresState,
        _errorMessage,
        rechercheApiState
    ) { liste, filtresLocaux, erreur, rechercheApi ->
        val (recherche, favoris, type) = filtresLocaux
        val queryApi = rechercheApi.query
        val searching = rechercheApi.isSearching
        val importError = rechercheApi.erreur
        val filtres = liste
            .filter { it.titre.contains(recherche, ignoreCase = true) }
            .filter { !favoris || it.estFavori }
            .filter { type == null || it.activite == type }

        EntrainementsUiState(
            entrainements = filtres,
            isLoading = false,
            errorMessage = erreur ?: importError,
            rechercheQuery = recherche,
            rechercheMoveBase = queryApi,
            isSearching = searching,
            rechercheApi = rechercheApi,
            filtreFavoris = favoris,
            typeActiviteSelectionne = type
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EntrainementsUiState(isLoading = true)
    )

    // Home screen: totals on the unfiltered list
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

    // Detail screen
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

    // Actions: they only write to a source
    fun onRechercheChange(query: String) { _rechercheQuery.value = query }
    fun onFiltreFavorisChange(favoris: Boolean) { _filtreFavoris.value = favoris }
    fun onTypeActiviteChange(type: TypeActivite?) { _typeActivite.value = type }

    fun basculerComplete(id: Int) {
        viewModelScope.launch { repository.basculerComplete(id) }
    }

    fun basculerFavori(id: Int) {
        viewModelScope.launch { repository.basculerFavori(id) }
    }

    fun supprimerEntrainement(id: Int) {
        viewModelScope.launch { repository.supprimer(id) }
    }

    fun chargerDetail(id: Int) { _idSelectionne.value = id }
    fun afficherDialogueSuppression() { _afficherDialogueSuppression.value = true }
    fun masquerDialogueSuppression() { _afficherDialogueSuppression.value = false }

    fun confirmerSuppression() {
        val id = _idSelectionne.value ?: return
        _afficherDialogueSuppression.value = false
        supprimerEntrainement(id)
    }

    fun onRechercheMoveBaseChange(query: String) {
        if (rechercheApiState.value.isSearching || rechercheApiState.value.idImportEnCours != null) return
        rechercheApiState.update { it.copy(query = query, erreur = null, resultats = emptyList(), afficherResultats = false) }
    }

    fun rechercherExercices() {
        val state = rechercheApiState.value
        val query = state.query.trim()
        if (query.isEmpty() || state.isSearching || state.idImportEnCours != null) return
        rechercheApiState.update { it.copy(isSearching = true, erreur = null, resultats = emptyList(), afficherResultats = false) }
        viewModelScope.launch {
            try {
                repository.rechercherExercicesDepuisApi(query, BuildConfig.API_KEY)
                    .onSuccess { exercices ->
                        rechercheApiState.update { it.copy(resultats = exercices, afficherResultats = true) }
                    }
                    .onFailure { e ->
                        rechercheApiState.update { it.copy(erreur = e.message ?: "Erreur lors de la recherche.") }
                    }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                rechercheApiState.update { it.copy(erreur = e.message ?: "Erreur lors de la recherche.") }
            } finally {
                rechercheApiState.update { it.copy(isSearching = false) }
            }
        }
    }

    fun importerExercice(exercice: ExerciceDTO) {
        val state = rechercheApiState.value
        if (state.isSearching || state.idImportEnCours != null || exercice.id in state.idsImportes) return
        if (state.resultats.none { it.id == exercice.id }) return
        rechercheApiState.update { it.copy(idImportEnCours = exercice.id, erreur = null) }
        viewModelScope.launch {
            try {
                repository.importerExercice(exercice)
                    .onSuccess {
                        rechercheApiState.update { it.copy(idsImportes = it.idsImportes + exercice.id) }
                        _rechercheQuery.value = ""
                        _filtreFavoris.value = false
                        _typeActivite.value = null
                    }
                    .onFailure { e ->
                        rechercheApiState.update { it.copy(erreur = e.message ?: "Erreur lors de l'importation.") }
                    }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                rechercheApiState.update { it.copy(erreur = e.message ?: "Erreur lors de l'importation.") }
            } finally {
                rechercheApiState.update { it.copy(idImportEnCours = null) }
            }
        }
    }

    fun fermerResultatsMoveBase() {
        if (rechercheApiState.value.idImportEnCours == null) {
            rechercheApiState.update { it.copy(afficherResultats = false) }
        }
    }

    fun effacerErreur() {
        _errorMessage.value = null
        rechercheApiState.update { it.copy(erreur = null) }
    }
}