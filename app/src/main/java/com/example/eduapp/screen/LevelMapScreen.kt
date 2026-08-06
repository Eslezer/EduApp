package com.example.eduapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.model.Puzzle
import com.example.eduapp.model.PuzzleCatalog
import com.example.eduapp.navigation.AppDestination
import com.example.eduapp.viewmodel.GameViewModel

@Composable
fun LevelMapScreen(navController: NavHostController, viewModel: GameViewModel) {
    val progress by viewModel.progress.collectAsState()
    val completedIds = progress.filter { it.completedAt != null }.map { it.puzzleId }.toSet()

    PuzzleScaffold(navController, AppDestination.Levels) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("CHOOSE YOUR TRAIL", modifier = Modifier.padding(top = 18.dp), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("Puzzle map", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text("Each completed challenge opens the next one.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 12.dp))
            }
            (1..3).forEach { chapter ->
                val chapterPuzzles = PuzzleCatalog.puzzles.filter { it.chapter == chapter }
                item { ChapterTitle(chapter, chapterPuzzles.count { it.id in completedIds }) }
                items(chapterPuzzles, key = { it.id }) { puzzle ->
                    val index = PuzzleCatalog.indexOf(puzzle.id)
                    val unlocked = index == 0 || PuzzleCatalog.puzzles[index - 1].id in completedIds || puzzle.id in completedIds
                    PuzzleMapCard(puzzle, unlocked, puzzle.id in completedIds) {
                        navController.navigate(AppDestination.Game)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChapterTitle(chapter: Int, solved: Int) {
    val name = when (chapter) {
        1 -> "Meadow maths"
        2 -> "Orchard logic"
        else -> "Summit challenge"
    }
    Column(Modifier.padding(top = 12.dp, bottom = 2.dp)) {
        Text("CHAPTER $chapter", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("$name - $solved / 6 solved", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PuzzleMapCard(puzzle: Puzzle, unlocked: Boolean, solved: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().alpha(if (unlocked) 1f else .55f).clickable(enabled = unlocked, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (solved) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(if (solved) "STAR EARNED" else if (unlocked) "READY TO PLAY" else "LOCKED", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(puzzle.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(puzzle.skill, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
