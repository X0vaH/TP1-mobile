package com.example.tp1.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp1.ui.theme.DemoFilmsTheme

@Composable
fun BtnDetails(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text("Details")
    }
}

@Preview(name = "Bouton détails")
@Composable
fun BtnDetailsPreview() {
    DemoFilmsTheme() {
        BtnDetails(
            onClick = {
                println("Click")
            }
    ) }

}
