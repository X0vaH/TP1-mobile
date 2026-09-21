package com.example.tp1.ui.theme.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp1.R
import com.example.tp1.ui.theme.TrainingApp

@Composable
fun BtnDetails(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
        Text(stringResource(R.string.txt_details))
    }
}

@Preview(name = "Bouton détails")
@Composable
fun BtnDetailsPreview() {
    TrainingApp {
        BtnDetails(
            onClick = {
                println("Click")
            }
    ) }

}
