package com.devspacecinenow

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun CineNowApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "movieList") {
        composable("movieList") {
            MovieListScreen(navController)
        }
        composable(
            "movieDetail" + "/{itemId}",
            arguments = listOf(navArgument("itemId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val movieId = requireNotNull(backStackEntry.arguments?.getString("itemId"))
            MovieDetailScreen(movieId, navController)
        }
    }
}