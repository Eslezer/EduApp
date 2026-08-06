@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.eduapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eduapp.navigation.AppDestination
import com.example.eduapp.model.PuzzleCatalog
import com.example.eduapp.screen.GameScreen
import com.example.eduapp.screen.LandingScreen
import com.example.eduapp.screen.LevelMapScreen
import com.example.eduapp.screen.ScoreScreen
import com.example.eduapp.screen.SettingScreen
import com.example.eduapp.screen.TestDBScreen
import com.example.eduapp.ui.theme.EduAppTheme
import com.example.eduapp.viewmodel.GameViewModel
import com.example.eduapp.viewmodel.GameViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val currentContext = applicationContext
        setContent {
            EduAppTheme {
                AppNav(currentContext)
            }
        }
    }
}
@Composable
fun AppNav(currentContext: Context) {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory(currentContext))
    NavHost(navController = navController, startDestination = AppDestination.Home) {
        composable(AppDestination.Home) { LandingScreen(navController, gameViewModel) }
        composable(AppDestination.Levels) { LevelMapScreen(navController, gameViewModel) }
        composable(AppDestination.Settings) { SettingScreen(navController) }
        composable(AppDestination.GamePattern) { entry ->
            val puzzleId = entry.arguments?.getString("puzzleId") ?: PuzzleCatalog.puzzles.first().id
            GameScreen(puzzleId, navController)
        }
        composable(AppDestination.Scores) { ScoreScreen(navController) }
        composable(AppDestination.TestDatabase) { TestDBScreen(currentContext) }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduAppTheme {

    }
}
