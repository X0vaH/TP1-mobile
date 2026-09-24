package com.example.tp1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tp1.R
import com.example.tp1.repository.FakeListEntrainement
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.ui.components.ProchaineSeanceCard
import com.example.tp1.viewmodel.AccueilUiState
import com.example.tp1.viewmodel.EntrainementsViewModel
import kotlin.math.roundToInt

@Composable
fun AccueilScreen(
    viewModel: EntrainementsViewModel,
    onSeanceClick: (Int) -> Unit,
    onListClick: () -> Unit,
    onCreerClick: () -> Unit
) {
    val uiState by viewModel.accueilUiState.collectAsStateWithLifecycle()

    AccueilContent(
        uiState = uiState,
        onSeanceClick = onSeanceClick,
        onListClick = onListClick,
        onCreerClick = onCreerClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccueilContent(
    uiState: AccueilUiState,
    onSeanceClick: (Int) -> Unit,
    onListClick: () -> Unit,
    onCreerClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { padding ->
        when {
            uiState.isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            uiState.errorMessage != null -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(stringResource(R.string.erreur_chargement, uiState.errorMessage)) }

            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.compteur_seances,
                        uiState.seancesCompletees,
                        uiState.totalSeances
                    ),
                    style = MaterialTheme.typography.titleLarge
                )

                LinearProgressIndicator(
                    progress = { uiState.progression },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    stringResource(
                        R.string.pourcentage_progression,
                        (uiState.progression * 100).roundToInt()
                    )
                )

                Text(
                    text = stringResource(R.string.titre_prochaine_seance),
                    style = MaterialTheme.typography.titleMedium
                )
                if (uiState.prochaineSeance != null) {
                    ProchaineSeanceCard(
                        entrainement = uiState.prochaineSeance,
                        onClick = { onSeanceClick(uiState.prochaineSeance.id) }
                    )
                } else {
                    Text(stringResource(R.string.aucune_seance_a_venir))
                }

                TextButton(onClick = onListClick) {
                    Text(stringResource(R.string.tous_les_entrainements))
                }

                Button(
                    onClick = onCreerClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.creer_entrainement_cd))
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AccueilPreview() {
    TrainingApp {
        AccueilContent(
            uiState = AccueilUiState(
                totalSeances = 3,
                seancesCompletees = 1,
                prochaineSeance = FakeListEntrainement.liste.first(),
                isLoading = false
            ),
            onSeanceClick = {},
            onListClick = {},
            onCreerClick = {}
        )
    }
}

@PreviewLightDark
@Composable
fun AccueilToutCompletePreview() {
    TrainingApp {
        AccueilContent(
            uiState = AccueilUiState(
                totalSeances = 3,
                seancesCompletees = 3,
                prochaineSeance = null,
                isLoading = false
            ),
            onSeanceClick = {},
            onListClick = {},
            onCreerClick = {}
        )
    }
}