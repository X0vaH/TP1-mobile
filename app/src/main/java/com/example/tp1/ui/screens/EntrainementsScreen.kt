package com.example.tp1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.example.tp1.repository.FakeListEntrainement
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.ui.theme.components.EntrainementCard
import com.example.tp1.viewmodel.EntrainementsUiState
import com.example.tp1.viewmodel.EntrainementsViewModel


@Composable
fun EntrainementsScreen(
    viewModel: EntrainementsViewModel,
    onDetailsClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EntrainementContent(
        uiState = uiState,
        onDetailsClick = { entrainement -> onDetailsClick(entrainement.id) }
    )
}

@Composable
fun EntrainementContent(uiState: EntrainementsUiState, onDetailsClick: (Entrainement) -> Unit) {
    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.erreur_chargement, uiState.errorMessage))
            }
        }
        else -> {
            EntrainementsListe(uiState.entrainements, onDetailsClick)
        }
    }
}
@Composable
fun EntrainementsListe(entrainements: List<Entrainement>, onDetailsClick: (Entrainement) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(top = 45.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        items(entrainements) {
            entrainement ->
            EntrainementCard(entrainement, onDetailsClick = {
                onDetailsClick(entrainement)
            })
        }
    }
}

@Preview(
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES
)
@Composable
fun EntrainementListePreview() {
    TrainingApp {
        EntrainementsListe(FakeListEntrainement.liste, onDetailsClick = {
            entrainement -> println("entrainement : ${entrainement.titre}")
        })
    }
}