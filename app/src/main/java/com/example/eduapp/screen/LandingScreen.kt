package com.example.eduapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.R
import com.example.eduapp.model.PuzzleCatalog
import com.example.eduapp.navigation.AppDestination
import com.example.eduapp.viewmodel.GameViewModel

@Composable
fun LandingScreen(navController: NavHostController, viewModel: GameViewModel) {
    val progress by viewModel.progress.collectAsState()
    val completed = progress.count { it.completedAt != null }

    PuzzleScaffold(navController, AppDestination.Home) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.smiley_puzzle_logo),
                    contentDescription = "Puzzle Trail logo",
                    modifier = Modifier.size(58.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("PUZZLE TRAIL", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Text("Train your brain,\none clue at a time.", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black)
            Text("Solve visual maths puzzles, collect stars, and unlock the next stop on your trail.", style = MaterialTheme.typography.bodyLarge)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text("YOUR ADVENTURE", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(8.dp))
                    Text("$completed / ${PuzzleCatalog.puzzles.size} puzzles solved", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    TrailProgress(completed, PuzzleCatalog.puzzles.size)
                    Spacer(Modifier.height(18.dp))
                    Button(onClick = { navController.navigate(AppDestination.Levels) }) {
                        Text(if (completed == 0) "Start the trail" else "Continue your trail")
                    }
                }
            }

            Text("HOW IT WORKS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickStep("1", "Study", "Look closely", Modifier.weight(1f))
                QuickStep("2", "Solve", "Enter an answer", Modifier.weight(1f))
                QuickStep("3", "Grow", "Earn stars", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun QuickStep(number: String, title: String, description: String, modifier: Modifier) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant).padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(number, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Text(description, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun TrailProgress(completed: Int, total: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(total) { index ->
            Text(
                text = if (index < completed) "★" else "☆",
                color = if (index < completed) Color(0xFFFFA000) else MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
