package com.example.tp1.ui.theme.screens

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
<<<<<<< HEAD
import androidx.compose.ui.res.stringResource
=======
>>>>>>> origin/1-feature---ui--training-creation-form
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
<<<<<<< HEAD
import com.example.tp1.R
import com.example.tp1.data.Entrainement
import com.example.tp1.repository.FakeListEntrainement
<<<<<<< HEAD:app/src/main/java/com/example/tp1/ui/screens/EntrainementsScreen.kt
import com.example.tp1.ui.theme.TrainingApp
=======
=======
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tp1.data.Entrainement
import com.example.tp1.repository.FakeListEntrainement
>>>>>>> origin/1-feature---ui--training-creation-form
import com.example.tp1.ui.theme.DemoFilmsTheme
import com.example.tp1.ui.theme.components.EntrainementCard
import com.example.tp1.viewmodel.EntrainementsUiState
import com.example.tp1.viewmodel.EntrainementsViewModel


@Composable
fun EntrainementsScreen(
<<<<<<< HEAD
    viewModel: EntrainementsViewModel,
    onDetailsClick: (Int) -> Unit
=======
    viewModel: EntrainementsViewModel = viewModel()
>>>>>>> origin/1-feature---ui--training-creation-form
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EntrainementContent(
        uiState = uiState,
<<<<<<< HEAD
        onDetailsClick = { entrainement -> onDetailsClick(entrainement.id) }
=======
        onDetailsClick = viewModel::selectionneEntrainement
>>>>>>> origin/1-feature---ui--training-creation-form
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
<<<<<<< HEAD
=======
                //Text("Chargement films.....")
>>>>>>> origin/1-feature---ui--training-creation-form
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
<<<<<<< HEAD
                Text(stringResource(R.string.erreur_chargement, uiState.errorMessage))
=======
                Text("erreur ${uiState.errorMessage}}")
>>>>>>> origin/1-feature---ui--training-creation-form
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
<<<<<<< HEAD
fun EntrainementListePreview() {
    TrainingApp {
=======
fun FilmsListePreview() {
    DemoFilmsTheme {
>>>>>>> origin/1-feature---ui--training-creation-form
        EntrainementsListe(FakeListEntrainement.liste, onDetailsClick = {
            entrainement -> println("entrainement : ${entrainement.titre}")
        })
    }
}