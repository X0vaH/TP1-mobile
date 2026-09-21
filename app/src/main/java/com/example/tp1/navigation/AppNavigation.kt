package com.example.tp1.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tp1.ui.theme.screens.EntrainementDetailScreen
import com.example.tp1.ui.theme.screens.EntrainementsScreen
import com.example.tp1.viewmodel.EntrainementsViewModel

object Routes {
    const val ARG_ID = "id"
    const val LISTE = "liste"
    const val DETAIL = "detail/{$ARG_ID}"

    fun detail(id: Int) = "detail/$id"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: EntrainementsViewModel = viewModel()
) {
    NavHost(navController = navController, startDestination = Routes.LISTE) {
        composable(Routes.LISTE) {
            EntrainementsScreen(
                viewModel = viewModel,
                onDetailsClick = { id -> navController.navigate(Routes.detail(id)) }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(Routes.ARG_ID) ?: return@composable
            EntrainementDetailScreen(
                id = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}