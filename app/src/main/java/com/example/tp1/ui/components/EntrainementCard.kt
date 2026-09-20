package com.example.tp1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tp1.data.Entrainement
import com.example.tp1.ui.theme.DemoFilmsTheme

@Composable
fun EntrainementCard(entrainement: Entrainement, onDetailsClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
        shape = CutCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        )
        {
            Column {
                Text(entrainement.titre)
                Text("Activité : " + entrainement.activite)
                Text("Lieu : " + entrainement.lieu)
                Text("A l'extérieur : " + entrainement.exterieur.toString())
                Text("Intensité : " + entrainement.intensite + "/10")
                Text("Notes : " + entrainement.notes)

                BtnDetails(
                    onClick = onDetailsClick )
            }

        }
        }

}

@Preview
@Composable
fun EntrainementCardPreview() {
    DemoFilmsTheme {
        EntrainementCard(Entrainement("Course a l'exterieur", "Course", "Piste de course",
            true, 8, "Apporter équipements pour la course"),
            onDetailsClick = {
                println("Details")
            })
    }

}