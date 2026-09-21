package com.example.tp1.ui.theme.screens

<<<<<<< HEAD
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
=======
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
>>>>>>> origin/1-feature---ui--training-creation-form
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.viewmodel.CreateEntrainementsUiState
import com.example.tp1.viewmodel.CreateEntrainementsViewModel

@OptIn(ExperimentalMaterial3Api::class)
=======
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tp1.viewmodel.CreateEntrainementsViewModel

>>>>>>> origin/1-feature---ui--training-creation-form
@Composable
fun CreateEntrainementScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
<<<<<<< HEAD
    onEntrainementCreated: () -> Unit,
=======
    onFilmCreated: () -> Unit,
>>>>>>> origin/1-feature---ui--training-creation-form
    viewModel: CreateEntrainementsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nouveau entrainement") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        CreateFilmContent(
            uiState = uiState,
            modifier = modifier.padding(innerPadding),
            onTitreChange = viewModel::modifierTitre,
            onAnneeChange = viewModel::modifierAnnee,
            onNoteChange = viewModel::modifierNote,
            onSaveClick = {
<<<<<<< HEAD
                viewModel.creerEntrainement(onEntrainementCreated)
=======
                viewModel.creerFilm(onFilmCreated)
>>>>>>> origin/1-feature---ui--training-creation-form
            }
        )
    }
}

@Composable
private fun CreateFilmContent(
<<<<<<< HEAD
    uiState: CreateEntrainementsUiState,
=======
    uiState: CreateFilmUiState,
>>>>>>> origin/1-feature---ui--training-creation-form
    modifier: Modifier = Modifier,
    onTitreChange: (String) -> Unit,
    onAnneeChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.titre,
            onValueChange = onTitreChange,
            label = { Text("Titre") },
            isError = uiState.erreurTitre != null,
            supportingText = {
                uiState.erreurTitre?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.annee,
            onValueChange = onAnneeChange,
            label = { Text("Année") },
            isError = uiState.erreurAnnee != null,
            supportingText = {
                uiState.erreurAnnee?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.note,
            onValueChange = onNoteChange,
            label = { Text("Note") },
            isError = uiState.erreurNote != null,
            supportingText = {
                uiState.erreurNote?.let {
                    Text(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            enabled = !uiState.isSaving,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Créer")
        }

        uiState.erreur?.let {
            Text(
                text = "Erreur : $it",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
<<<<<<< HEAD
}
@Preview
@Composable
fun CreateEntrainementScreenPreview() {
    TrainingApp {
        CreateEntrainementScreen()
    }
=======
>>>>>>> origin/1-feature---ui--training-creation-form
}