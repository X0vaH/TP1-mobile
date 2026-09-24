package com.example.tp1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import com.example.tp1.ui.theme.screens.AccueilScreen
import com.example.tp1.ui.theme.screens.CreateEntrainementScreen
import com.example.tp1.ui.theme.screens.EntrainementDetailScreen
import com.example.tp1.ui.theme.screens.EntrainementsScreen
import com.example.tp1.viewmodel.CreateEntrainementsViewModel
import com.example.tp1.viewmodel.EntrainementsViewModel

object Routes {
    const val ARG_ID = "id"
    const val ACCUEIL = "accueil"
    const val LISTE = "liste"
    const val DETAIL = "detail/{$ARG_ID}"
    const val CREER = "CREER"

    fun detail(id: Int) = "detail/$id"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    // Created ONCE for the whole app. Both ViewModels below read/write this same instance.
    val repository: IEntrainementRepository = remember { FakeEntrainementRepository() }
    val viewModel: EntrainementsViewModel = viewModel { EntrainementsViewModel(repository) }

    NavHost(navController = navController, startDestination = Routes.ACCUEIL) {
        composable(Routes.ACCUEIL) {
            AccueilScreen(
                viewModel = viewModel,
                onSeanceClick = { id -> navController.navigate(Routes.detail(id)) },
                onListClick = { navController.navigate(Routes.LISTE) },
                onCreerClick = { navController.navigate(Routes.CREER) }
            )
        }
        composable(Routes.LISTE) {
            EntrainementsScreen(
                viewModel = viewModel,
                onDetailsClick = { id -> navController.navigate(Routes.detail(id)) },
                onCreateClick = { navController.navigate(Routes.CREER) }
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
        composable(route = Routes.CREER) {
            val createViewModel: CreateEntrainementsViewModel =
                viewModel { CreateEntrainementsViewModel(repository) }
            CreateEntrainementScreen(
                modifier = Modifier,
                onBackClick = { navController.popBackStack() },
                onEntrainementCreated = { navController.popBackStack() },
                viewModel = createViewModel
            )
        }
    }
}