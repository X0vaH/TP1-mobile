package com.example.tp1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tp1.R
import com.example.tp1.network.ExerciceDTO
import com.example.tp1.viewmodel.RechercheMoveBaseUiState
import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import com.example.tp1.repository.FakeListEntrainement
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.ui.components.EntrainementCard
import com.example.tp1.viewmodel.EntrainementsUiState
import com.example.tp1.viewmodel.EntrainementsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrainementsScreen(
    viewModel: EntrainementsViewModel,
    onDetailsClick: (Int) -> Unit,
    onCreateClick: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton (onClick = onBack) {
                        Icon (
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.retour)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.entrainement_introuvable)
                )
            }
        },
    ) { innerPadding ->
        EntrainementContent(
            uiState = uiState,
            modifier = Modifier.padding(innerPadding),
            onDetailsClick = { entrainement -> onDetailsClick(entrainement.id) },
            onFavoriClick = { entrainement -> viewModel.basculerFavori(entrainement.id) },
            onRechercheChange = viewModel::onRechercheChange,
            onFiltreFavorisChange = viewModel::onFiltreFavorisChange,
            onTypeActiviteChange = viewModel::onTypeActiviteChange,
            onRechercheMoveBaseChange = viewModel::onRechercheMoveBaseChange,
            onRechercherClick = viewModel::rechercherExercices,
            onAjouterExercice = viewModel::importerExercice,
            onFermerResultats = viewModel::fermerResultatsMoveBase
        )
    }
}

@Composable
fun EntrainementContent(
    uiState: EntrainementsUiState,
    modifier: Modifier = Modifier,
    onDetailsClick: (Entrainement) -> Unit,
    onFavoriClick: (Entrainement) -> Unit,
    onRechercheChange: (String) -> Unit,
    onFiltreFavorisChange: (Boolean) -> Unit,
    onTypeActiviteChange: (TypeActivite?) -> Unit,
    onRechercheMoveBaseChange: (String) -> Unit = {},
    onRechercherClick: () -> Unit = {},
    onAjouterExercice: (ExerciceDTO) -> Unit = {},
    onFermerResultats: () -> Unit = {}
) {
    if (uiState.rechercheApi.afficherResultats) {
        ResultatsMoveBase(
            state = uiState.rechercheApi,
            onAjouter = onAjouterExercice,
            onFermer = onFermerResultats
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BarreRechercheMoveBase(
            query = uiState.rechercheMoveBase,
            isSearching = uiState.isSearching,
            onQueryChange = onRechercheMoveBaseChange,
            onRechercherClick = onRechercherClick
        )
        uiState.errorMessage?.let { message ->
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
        // Search field
        OutlinedTextField(
            value = uiState.rechercheQuery,
            onValueChange = onRechercheChange,
            label = { Text(stringResource(R.string.rechercher_titre)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.recherche_cd)
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Filter chips (Favoris + Types d'activité)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                FilterChip(
                    selected = uiState.filtreFavoris,
                    onClick = { onFiltreFavorisChange(!uiState.filtreFavoris) },
                    label = { Text(stringResource(R.string.filtre_favoris)) },
                    leadingIcon = if (uiState.filtreFavoris) {
                        {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }

            item {
                FilterChip(
                    selected = uiState.typeActiviteSelectionne == null,
                    onClick = { onTypeActiviteChange(null) },
                    label = { Text(stringResource(R.string.filtre_tous)) }
                )
            }

            items(TypeActivite.entries) { activite ->
                FilterChip(
                    selected = uiState.typeActiviteSelectionne == activite,
                    onClick = { onTypeActiviteChange(activite) },
                    label = { Text(stringResource(activite.labelRes)) }
                )
            }
        }

        EntrainementsListe(
            entrainements = uiState.entrainementsFiltres,
            onDetailsClick = onDetailsClick,
            onFavoriClick = onFavoriClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun EntrainementsListe(
    entrainements: List<Entrainement>,
    onDetailsClick: (Entrainement) -> Unit,
    onFavoriClick: (Entrainement) -> Unit,
    modifier: Modifier = Modifier
) {
    if (entrainements.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.aucun_entrainement_trouve))
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(entrainements) { entrainement ->
                EntrainementCard(
                    entrainement = entrainement,
                    onDetailsClick = { onDetailsClick(entrainement) },
                    onFavoriClick = { onFavoriClick(entrainement) }
                )
            }
        }
    }
}

@Composable
private fun BarreRechercheMoveBase(
    query: String,
    isSearching: Boolean,
    onQueryChange: (String) -> Unit,
    onRechercherClick: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        enabled = !isSearching,
        label = { Text("Rechercher sur MoveBase (ex. : squat)") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (isSearching) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else if (query.isNotBlank()) {
                IconButton(onClick = onRechercherClick) {
                    Icon(Icons.Default.Search, contentDescription = "Rechercher les exercices")
                }
            }
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
private fun ResultatsMoveBase(
    state: RechercheMoveBaseUiState,
    onAjouter: (ExerciceDTO) -> Unit,
    onFermer: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onFermer,
        title = { Text("Résultats MoveBase (${state.resultats.size})") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.erreur?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (state.resultats.isEmpty()) {
                    Text("Aucun exercice trouvé pour « ${state.query.trim()} ».")
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.resultats, key = { it.id }) { exercice ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(exercice.name, style = MaterialTheme.typography.titleSmall)
                                    Text("${exercice.target} • ${exercice.equipment}")
                                    Text(exercice.instructions, maxLines = 3)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        val dejaAjoute = exercice.id in state.idsImportes
                                        TextButton(
                                            onClick = { onAjouter(exercice) },
                                            enabled = state.idImportEnCours == null && !dejaAjoute
                                        ) {
                                            if (state.idImportEnCours == exercice.id) {
                                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                            } else {
                                                Text(if (dejaAjoute) "Ajouté" else "Ajouter")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onFermer, enabled = state.idImportEnCours == null) {
                Text("Fermer")
            }
        }
    )
}
@Preview(name = "Clair")
@Preview(name = "Sombre", uiMode = AndroidUiModes.UI_MODE_NIGHT_YES)
@Composable
fun EntrainementListePreview() {
    TrainingApp {
        EntrainementContent(
            uiState = EntrainementsUiState(entrainements = FakeListEntrainement.liste),
            onDetailsClick = { },
            onFavoriClick = { },
            onRechercheChange = { },
            onFiltreFavorisChange = { },
            onTypeActiviteChange = { }
        )
    }
}
