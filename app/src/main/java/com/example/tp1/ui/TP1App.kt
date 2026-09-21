package com.example.tp1.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tp1.ui.navigation.CreateEntrainementRoute
import com.example.tp1.ui.navigation.EntrainementRoute
import com.example.tp1.ui.theme.screens.EntrainementsScreen

@Composable
fun TP1App() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = EntrainementRoute
    ) {
        composable<EntrainementRoute> {
            EntrainementsScreen()
        }
        composable<CreateEntrainementRoute> {
            // TODO : Add link to create screen
        }
    }
}