package com.example.tp1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.tp1.repository.FakeEntrainementRepository
import com.example.tp1.repository.IEntrainementRepository
import com.example.tp1.ui.screens.AccueilScreen
import com.example.tp1.ui.screens.CreateEntrainementScreen
import com.example.tp1.ui.screens.EntrainementDetailScreen
import com.example.tp1.ui.screens.EntrainementsScreen
import com.example.tp1.viewmodel.CreateEntrainementsViewModel
import com.example.tp1.viewmodel.EntrainementsViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    // Created ONCE for the whole app. Both ViewModels below read/write this same instance.
    val repository: IEntrainementRepository = remember { FakeEntrainementRepository() }
    val viewModel: EntrainementsViewModel = viewModel { EntrainementsViewModel(repository) }

    NavHost(navController = navController, startDestination = AccueilRoute) {
        composable<AccueilRoute> {
            AccueilScreen(
                viewModel = viewModel,
                onSeanceClick = { id -> navController.navigate(DetailRoute(id)) },
                onListClick = { navController.navigate(ListeRoute) },
                onCreerClick = { navController.navigate(CreerRoute) }
            )
        }
        composable<ListeRoute> {
            EntrainementsScreen(
                viewModel = viewModel,
                onDetailsClick = { id -> navController.navigate(DetailRoute(id)) },
                onCreateClick = { navController.navigate(CreerRoute) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<DetailRoute> { backStackEntry ->
            val route: DetailRoute = backStackEntry.toRoute()
            EntrainementDetailScreen(
                id = route.id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable<CreerRoute> {
            val createViewModel: CreateEntrainementsViewModel =
                viewModel { CreateEntrainementsViewModel(repository) }
            CreateEntrainementScreen(
                onBackClick = { navController.popBackStack() },
                onEntrainementCreated = { navController.popBackStack() },
                viewModel = createViewModel
            )
        }
    }
}