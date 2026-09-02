package com.vocabplus.app.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun VocabNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(modifier = modifier) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                StubScreen(
                    title = "Home Screen",
                    onNavigate = { navController.navigate(Screen.Quiz.createRoute(com.vocabplus.app.domain.model.Category.SYNONYM)) }
                )
            }

            composable(
                route = Screen.Quiz.route,
                arguments = listOf(navArgument("categorySlug") { type = NavType.StringType })
            ) { backStackEntry ->
                val categorySlug = backStackEntry.arguments?.getString("categorySlug").orEmpty()
                StubScreen(
                    title = "Quiz: $categorySlug",
                    onNavigate = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.SectionResult.route,
                arguments = listOf(
                    navArgument("categorySlug") { type = NavType.StringType },
                    navArgument("score") { type = NavType.IntType },
                    navArgument("total") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("categorySlug")
                val score = backStackEntry.arguments?.getInt("score") ?: 0
                val total = backStackEntry.arguments?.getInt("total") ?: 10
                StubScreen(
                    title = "Section Result: $category ($score / $total)",
                    onNavigate = { navController.popBackStack(Screen.Home.route, false) }
                )
            }

            composable(Screen.DailySummary.route) {
                StubScreen(title = "Daily Summary", onNavigate = { navController.popBackStack() })
            }

            composable(Screen.Revision.route) {
                StubScreen(title = "Revision", onNavigate = { navController.popBackStack() })
            }

            composable(Screen.Statistics.route) {
                StubScreen(title = "Statistics", onNavigate = { navController.popBackStack() })
            }

            composable(Screen.Settings.route) {
                StubScreen(title = "Settings", onNavigate = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun StubScreen(title: String, onNavigate: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}