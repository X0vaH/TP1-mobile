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
    const val LISTE = "liste" // String pour le nom de la route liste.
    const val DETAIL = "detail/{$ARG_ID}" // Pattern de la route detail

    fun detail(id: Int) = "detail/$id" // Fonction avec le id pour naviger vers la vrai page.
}


@Composable
fun AppNavigation(
    // Singleton qui garde le controle de la page actuel et qui peux reculer et avancer a travers les pages
    navController: NavHostController = rememberNavController(),
    // View model creer une fois ici qui permet de ne pas en avoir plusieurs.
    viewModel: EntrainementsViewModel = viewModel()
) {
    // NavHost est le container de l'ecran et montre l'ecran choisis
    // StartDestination est l'ecran de depart **alors il faut le changer quand on aura le dashboard**
    NavHost(navController = navController, startDestination = Routes.LISTE) {
        // Declare l'ecran et lui passe le singleton du viewmodel
        composable(Routes.LISTE) {
            EntrainementsScreen(
                viewModel = viewModel,
                // Dit au nav controller de prendre le id de l'entrainement clicker et le passe au details
                onDetailsClick = { id -> navController.navigate(Routes.detail(id)) }
            )
        }
        // Declare la route detail.
        composable(
            // Prends le patern de details des const
            route = Routes.DETAIL,
            // Dit au nav que le {id} dans le pattern est un int
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            // BackstackEntry contient la route ou on est presentement dont le detail/2
            // On extract le 2 avant de le loader dans la vrai ecran de details
            val id = backStackEntry.arguments?.getInt(Routes.ARG_ID) ?: return@composable
            // Return l'ecran de details avec le bon id et en lui passant aussi le singleton du viewmodel
            EntrainementDetailScreen(
                id = id,
                viewModel = viewModel,
                // Nous permet de retourner sur le dernier l'ecran avec le navcontroller.
                onBack = { navController.popBackStack() }
            )
        }
    }
}