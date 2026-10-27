package com.example.tp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1.BuildConfig
import com.example.tp1.network.ExerciceDTO
import kotlinx.coroutines.flow.update
import com.example.tp1.data.TypeActivite
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
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
    private val _consignes = MutableStateFlow(ConsignesUiState())
    private val _reessaiConsignes = MutableStateFlow(0)

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

    // Detail screen: la séance est lue avec ses exercices
    private val entrainementDetailFlow = _idSelectionne.flatMapLatest { id ->
        if (id == null) flowOf(null) else repository.observerAvecExercices(id)
    }.catch { e ->
        _errorMessage.value = e.message
        emit(null)
    }

    // Les consignes ne sont rechargées que si la liste des exercices change (pas à chaque modification de la séance)
    private val idsExercicesFlow = entrainementDetailFlow
        .map { entrainement -> entrainement?.exercices?.map { it.idExterne }?.distinct().orEmpty() }
        .distinctUntilChanged()

    init {
        viewModelScope.launch {
            combine(idsExercicesFlow, _reessaiConsignes) { ids, _ -> ids }
                .collectLatest { ids -> chargerConsignes(ids) }
        }
    }

    val detailUiState: StateFlow<EntrainementDetailUiState> = combine(
        entrainementDetailFlow, _afficherDialogueSuppression, _consignes
    ) { entrainement, dialogue, consignes ->
        EntrainementDetailUiState(
            entrainement = entrainement,
            isLoading = false,
            afficherDialogueSuppression = dialogue,
            consignes = consignes
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

    fun reessayerConsignes() { _reessaiConsignes.update { it + 1 } }

    private suspend fun chargerConsignes(idsExercices: List<String>) {
        // Les consignes déjà chargées sont conservées
        val manquants = idsExercices.filter { it !in _consignes.value.consignes }
        if (manquants.isEmpty()) {
            _consignes.update { it.copy(isLoading = false, erreur = null) }
            return
        }

        _consignes.update { it.copy(isLoading = true, erreur = null) }
        try {
            for (idExercice in manquants) {
                repository.obtenirConsignesExercice(idExercice, BuildConfig.API_KEY)
                    .onSuccess { exercice ->
                        _consignes.update {
                            it.copy(consignes = it.consignes + (idExercice to exercice.instructions))
                        }
                    }
                    .onFailure { e ->
                        _consignes.update { it.copy(erreur = e.message ?: e.toString()) }
                        // On s'arrête au premier échec : le bouton Réessayer reprend les consignes manquantes
                        return
                    }
            }
        } finally {
            _consignes.update { it.copy(isLoading = false) }
        }
    }
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