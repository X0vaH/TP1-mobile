package com.example.tp1.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp1.R
import com.example.tp1.data.Entrainement
import com.example.tp1.data.TypeActivite
import com.example.tp1.ui.theme.TrainingApp

@Composable
fun EntrainementCard(
    entrainement: Entrainement,
    onDetailsClick: () -> Unit,
    onFavoriClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onDetailsClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = entrainement.titre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.card_activite, stringResource(entrainement.activite.labelRes)),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.card_lieu, entrainement.lieu),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onFavoriClick) {
                Icon(
                    imageVector = if (entrainement.estFavori) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (entrainement.estFavori) "Retirer des favoris" else "Ajouter aux favoris",
                    tint = if (entrainement.estFavori) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
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
            onDetailsClick = { println("Details") },
            onFavoriClick = { println("Favori") }
        )
    }
}
