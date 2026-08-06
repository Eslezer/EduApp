package com.example.eduapp.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.eduapp.navigation.AppDestination

private data class NavigationItem(val route: String, val label: String, val symbol: String)

private val navigationItems = listOf(
    NavigationItem(AppDestination.Home, "Home", "H"),
    NavigationItem(AppDestination.Game, "Play", "P"),
    NavigationItem(AppDestination.Scores, "Progress", "S"),
    NavigationItem(AppDestination.Settings, "Settings", "C")
)

/** Shared app frame used by all player-facing Puzzle Trail screens. */
@Composable
fun PuzzleScaffold(
    navController: NavHostController,
    selectedRoute: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedRoute == item.route,
                        onClick = {
                            if (selectedRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(AppDestination.Home) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Text(item.symbol) },
                        label = { Text(item.label) }
                    )
                }
            }
        },
        content = content
    )
}
