package com.example.tp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
<<<<<<< HEAD
import com.example.tp1.navigation.AppNavigation
import com.example.tp1.ui.theme.TrainingApp
=======
import com.example.tp1.ui.TP1App
import com.example.tp1.ui.theme.DemoFilmsTheme
>>>>>>> origin/1-feature---ui--training-creation-form


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
<<<<<<< HEAD
            TrainingApp {
                AppNavigation()
=======
            DemoFilmsTheme {
                TP1App()
>>>>>>> origin/1-feature---ui--training-creation-form
            }
        }
    }
}
