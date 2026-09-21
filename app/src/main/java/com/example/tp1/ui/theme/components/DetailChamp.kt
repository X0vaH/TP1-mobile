package com.example.tp1.ui.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp1.ui.theme.DemoFilmsTheme

@Composable
fun DetailChamp(label: String, valeur: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = valeur,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun DetailChampPreview() {
    DemoFilmsTheme {
        DetailChamp(label = "Lieu", valeur = "Gym (Intérieur)")
    }
}