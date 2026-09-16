package com.example.tp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tp1.ui.theme.DemoFilmsTheme
import com.example.tp1.ui.theme.screens.EntrainementsScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoFilmsTheme {
                EntrainementsScreen()
            }
        }
    }
}
