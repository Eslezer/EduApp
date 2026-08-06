package com.example.eduapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.model.PuzzleCatalog
import com.example.eduapp.navigation.AppDestination
import com.example.eduapp.viewmodel.GameViewModel

@Composable
fun ScoreScreen(navController: NavHostController, viewModel: GameViewModel) {
    val progress by viewModel.progress.collectAsState()
    val solved = progress.filter { it.completedAt != null }
    val points = solved.sumOf { it.bestScore }

    PuzzleScaffold(navController, AppDestination.Scores) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("YOUR PROGRESS", Modifier.padding(top = 18.dp), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("Trail journal", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("$points", "total points", Modifier.weight(1f))
                    MetricCard("${solved.size}/${PuzzleCatalog.puzzles.size}", "puzzles solved", Modifier.weight(1f))
                }
            }
            item {
                Text(
                    text = if (solved.isEmpty()) "Solve your first puzzle to start your journal." else "Completed puzzles",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            items(solved, key = { it.puzzleId }) { entry ->
                val puzzle = PuzzleCatalog.byId(entry.puzzleId)
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("STAR ${puzzle.title}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(puzzle.skill, style = MaterialTheme.typography.bodyMedium)
                        }
                        Text("${entry.bestScore} pts", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(value: String, label: String, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}
