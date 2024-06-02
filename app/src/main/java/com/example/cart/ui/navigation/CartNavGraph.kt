package com.example.cart.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cart.ui.home.HomeDestination
import com.example.cart.ui.home.HomeScreen
import com.example.cart.ui.detail.DetailDetailsDestination
import com.example.cart.ui.detail.DetailDetailsScreen
import com.example.cart.ui.detail.DetailEntryDestination
import com.example.cart.ui.detail.DetailEntryScreen


@Composable
fun CartNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToDetailEntry = { navController.navigate(DetailEntryDestination.route) },
                navigateToDetailUpdate = {
                    navController.navigate("${DetailDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = DetailEntryDestination.route) {
            DetailEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = DetailDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailDetailsDestination.detailIdArg) {
                type = NavType.IntType
            })
        ) {
            DetailDetailsScreen(
                navigateToEditDetail = { navController.navigate("${route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }

    }
}
