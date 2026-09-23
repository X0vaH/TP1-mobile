package com.example.tp1.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import com.example.tp1.repository.FakeListEntrainement
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.ui.theme.components.EntrainementCard
import com.example.tp1.viewmodel.EntrainementsUiState
import com.example.tp1.viewmodel.EntrainementsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrainementsScreen(
    viewModel: EntrainementsViewModel,
    onDetailsClick: (Int) -> Unit,
    onCreateClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Créer un entraînement"
                )
            }
        }
    ) { innerPadding ->
        EntrainementContent(
            uiState = uiState,
            modifier = Modifier.padding(innerPadding),
            onDetailsClick = { entrainement -> onDetailsClick(entrainement.id) },
            onFavoriClick = { entrainement -> viewModel.basculerFavori(entrainement.id) },
            onRechercheChange = viewModel::onRechercheChange,
            onFiltreFavorisChange = viewModel::onFiltreFavorisChange,
            onTypeActiviteChange = viewModel::onTypeActiviteChange
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
    onTypeActiviteChange: (TypeActivite?) -> Unit
) {
    when {
        uiState.isLoading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.erreur_chargement, uiState.errorMessage))
            }
        }
        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.rechercheQuery,
                    onValueChange = onRechercheChange,
                    label = { Text("Rechercher par titre") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Recherche"
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.filtreFavoris,
                            onClick = { onFiltreFavorisChange(!uiState.filtreFavoris) },
                            label = { Text("Favoris") },
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
                            label = { Text("Tous") }
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
            Text("Aucun entraînement trouvé")
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

@Preview(
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES
)
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
