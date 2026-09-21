package com.example.tp1.ui.theme.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
<<<<<<< HEAD
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp1.R
import com.example.tp1.ui.theme.TrainingApp
=======
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp1.ui.theme.DemoFilmsTheme
>>>>>>> origin/1-feature---ui--training-creation-form

@Composable
fun BtnDetails(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick
    ) {
<<<<<<< HEAD
        Text(stringResource(R.string.txt_details))
=======
        Text("Details")
>>>>>>> origin/1-feature---ui--training-creation-form
    }
}

@Preview(name = "Bouton détails")
@Composable
fun BtnDetailsPreview() {
<<<<<<< HEAD
    TrainingApp {
=======
    DemoFilmsTheme() {
>>>>>>> origin/1-feature---ui--training-creation-form
        BtnDetails(
            onClick = {
                println("Click")
            }
    ) }

}
