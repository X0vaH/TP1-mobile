package com.example.tp1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp1.R
import com.example.tp1.data.ExerciceSeance
import com.example.tp1.ui.theme.TrainingApp
import com.example.tp1.viewmodel.ConsignesUiState

@Composable
fun ExercicesDetailSection(
    exercices: List<ExerciceSeance>,
    consignes: ConsignesUiState,
    onReessayerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.exercices_titre),
            style = MaterialTheme.typography.titleMedium
        )

        if (exercices.isEmpty()) {
            Text(stringResource(R.string.aucun_exercice))
        }

        exercices.forEach { exercice ->
            ExerciceDetailCard(
                exercice = exercice,
                consigne = consignes.consignes[exercice.idExterne]
            )
        }

        if (consignes.isLoading) {
            val descriptionChargement = stringResource(R.string.chargement_consignes_cd)
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .semantics { contentDescription = descriptionChargement }
            )
        }

        consignes.erreur?.let { erreur ->
            Text(
                text = stringResource(R.string.erreur_chargement, erreur),
                color = MaterialTheme.colorScheme.error
            )
            OutlinedButton(onClick = onReessayerClick) {
                Text(stringResource(R.string.reessayer))
            }
        }
    }
}

@Composable
private fun ExerciceDetailCard(
    exercice: ExerciceSeance,
    consigne: String?
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(exercice.nom, style = MaterialTheme.typography.titleSmall)
            Text(
                stringResource(R.string.exercice_cible, exercice.cible),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                stringResource(R.string.exercice_equipement, exercice.equipement),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                stringResource(R.string.exercice_series_repetitions, exercice.series, exercice.repetitions),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                stringResource(R.string.exercice_charge, exercice.charge.toString().removeSuffix(".0")),
                style = MaterialTheme.typography.bodyMedium
            )
            if (consigne != null) {
                Text(consigne, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

private val exercicesApercu = listOf(
    ExerciceSeance(
        idExterne = "0024", nom = "barbell bench front squat", cible = "quads",
        equipement = "barbell", series = 3, repetitions = 10, charge = 60.0
    )
)

@Preview(name = "Chargement", uiMode = AndroidUiModes.UI_MODE_NIGHT_NO)
@Composable
fun ExercicesDetailSectionChargementPreview() {
    TrainingApp {
        ExercicesDetailSection(
            exercices = exercicesApercu,
            consignes = ConsignesUiState(isLoading = true),
            onReessayerClick = {}
        )
    }
}

@Preview(name = "Erreur", uiMode = AndroidUiModes.UI_MODE_NIGHT_NO)
@Composable
fun ExercicesDetailSectionErreurPreview() {
    TrainingApp {
        ExercicesDetailSection(
            exercices = exercicesApercu,
            consignes = ConsignesUiState(erreur = "Impossible de joindre MoveBase."),
            onReessayerClick = {}
        )
    }
}

@Preview(name = "Succès", uiMode = AndroidUiModes.UI_MODE_NIGHT_NO)
@Composable
fun ExercicesDetailSectionSuccesPreview() {
    TrainingApp {
        ExercicesDetailSection(
            exercices = exercicesApercu,
            consignes = ConsignesUiState(consignes = mapOf("0024" to "Commence debout, les pieds écartés...")),
            onReessayerClick = {}
        )
    }
}

@Preview(name = "Aucun exercice", uiMode = AndroidUiModes.UI_MODE_NIGHT_NO)
@Composable
fun ExercicesDetailSectionVidePreview() {
    TrainingApp {
        ExercicesDetailSection(
            exercices = emptyList(),
            consignes = ConsignesUiState(),
            onReessayerClick = {}
        )
    }
}
