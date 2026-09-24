package com.example.tp1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tp1.R
import com.example.tp1.data.Entrainement
import com.example.tp1.repository.FakeListEntrainement
import com.example.tp1.ui.theme.TrainingApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProchaineSeanceCard(
    entrainement: Entrainement,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = entrainement.titre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(stringResource(R.string.card_activite, stringResource(entrainement.activite.labelRes)))
            Text(stringResource(R.string.card_lieu, entrainement.lieu))
            Text(stringResource(R.string.card_intensite, entrainement.intensite))
        }
    }
}

@PreviewLightDark
@Composable
fun ProchaineSeanceCardPreview() {
    TrainingApp {
        ProchaineSeanceCard(
            entrainement = FakeListEntrainement.liste.first(),
            onClick = {}
        )
    }
}