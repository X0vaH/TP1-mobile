package com.example.tp1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp1.data.Entrainement
import androidx.compose.ui.res.stringResource
import com.example.tp1.R
import com.example.tp1.data.TypeActivite
import com.example.tp1.ui.theme.TrainingApp

@Composable
fun EntrainementCard(entrainement: Entrainement, onDetailsClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
        shape = CutCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        )
        {
            Column {
                Text(entrainement.titre)
                Text(stringResource(R.string.card_activite, stringResource(entrainement.activite.labelRes)))
                Text(stringResource(R.string.card_lieu, entrainement.lieu))
                Text(
                    stringResource(
                        R.string.card_exterieur,
                        stringResource(if (entrainement.exterieur) R.string.oui else R.string.non)
                    )
                )
                Text(stringResource(R.string.card_intensite, entrainement.intensite))
                Text(stringResource(R.string.card_notes, entrainement.notes))

                BtnDetails(onClick = onDetailsClick)
            }

        }
        }

}

@Preview
@Composable
fun EntrainementCardPreview() {
    TrainingApp {
        EntrainementCard(
            Entrainement(
                id = 1, titre = "Course à l'extérieur", activite = TypeActivite.COURSE,
                lieu = "Piste de course", exterieur = true, intensite = 8,
                notes = "Apporter équipements pour la course"
            ),
            onDetailsClick = { println("Details") }
        )
    }

}