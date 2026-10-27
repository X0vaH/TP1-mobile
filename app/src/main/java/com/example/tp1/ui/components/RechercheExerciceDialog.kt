package com.example.tp1.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tp1.R
import com.example.tp1.network.ExerciceDTO
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.viewmodel.RechercheExerciceUiState

@Composable
fun RechercheExerciceDialog(
    uiState: RechercheExerciceUiState,
    onQueryChange: (String) -> Unit,
    onRechercherClick: () -> Unit,
    onExerciceClick: (ExerciceDTO) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.recherche_exercice_titre),
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = onQueryChange,
                    label = { Text(stringResource(R.string.recherche_exercice_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = onRechercherClick,
                    enabled = uiState.query.isNotBlank() && !uiState.isSearching,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.rechercher))
                }

                when {
                    uiState.isSearching -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    uiState.erreur != null -> {
                        Text(
                            text = uiState.erreur,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    uiState.rechercheEffectuee && uiState.resultats.isEmpty() -> {
                        Text(stringResource(R.string.aucun_resultat_exercice))
                    }

                    else -> {
                        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                            items(uiState.resultats, key = { it.id }) { exercice ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onExerciceClick(exercice) }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(exercice.name, style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        stringResource(R.string.exercice_cible, exercice.target),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.fermer))
                    }
                }
            }
        }
    }
}

@Preview(uiMode = AndroidUiModes.UI_MODE_NIGHT_NO)
@Composable
fun RechercheExerciceDialogPreview() {
    TrainingApp {
        RechercheExerciceDialog(
            uiState = RechercheExerciceUiState(
                afficherDialogue = true,
                query = "squat",
                rechercheEffectuee = true,
                resultats = listOf(
                    ExerciceDTO(
                        id = "1", slug = "squat", name = "Squat", instructions = "",
                        target = "Jambes", category = "force", equipment = "Barre",
                        lang = "fr", attribution = "", createdAt = ""
                    )
                )
            ),
            onQueryChange = {},
            onRechercherClick = {},
            onExerciceClick = {},
            onDismiss = {}
        )
    }
}
