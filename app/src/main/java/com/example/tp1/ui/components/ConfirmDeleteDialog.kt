package com.example.tp1.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp1.R
import com.example.tp1.ui.theme.TrainingApp

@Composable
fun ConfirmationSuppressionDialog(
    titreEntrainement: String,
    onAnnuler: () -> Unit,
    onConfirmer: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onAnnuler,
        title = { Text(stringResource(R.string.dialogue_suppression_titre)) },
        text = { Text(stringResource(R.string.dialogue_suppression_message, titreEntrainement)) },
        confirmButton = {
            TextButton(onClick = onConfirmer) {
                Text(stringResource(R.string.supprimer))
            }
        },
        dismissButton = {
            TextButton(onClick = onAnnuler) {
                Text(stringResource(R.string.annuler))
            }
        }
    )
}

@Preview
@Composable
fun ConfirmationSuppressionDialogPreview() {
    TrainingApp {
        ConfirmationSuppressionDialog(
            titreEntrainement = "Course à l'extérieur",
            onAnnuler = {},
            onConfirmer = {}
        )
    }
}