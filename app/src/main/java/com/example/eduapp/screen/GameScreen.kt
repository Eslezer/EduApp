package com.example.eduapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.helper.rememberAssetImage
import com.example.eduapp.model.PuzzleCatalog
import com.example.eduapp.navigation.AppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(puzzleId: String, navController: NavHostController) {
    val puzzle = remember(puzzleId) { PuzzleCatalog.byId(puzzleId) }
    val image = rememberAssetImage(puzzle.imagePath)
    val position = PuzzleCatalog.indexOf(puzzle.id) + 1

    PuzzleScaffold(navController, AppDestination.Levels) { outerPadding ->
        Scaffold(topBar = {
            TopAppBar(
                title = { Text("Puzzle $position of ${PuzzleCatalog.puzzles.size}", fontWeight = FontWeight.Bold) },
                navigationIcon = { TextButton(onClick = { navController.popBackStack() }) { Text("Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(outerPadding).padding(innerPadding)
                    .padding(horizontal = 20.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("CHAPTER ${puzzle.chapter} - ${puzzle.skill.uppercase()}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(puzzle.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Text("Study the picture and work out the value of the question mark.", style = MaterialTheme.typography.bodyLarge)
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    if (image != null) {
                        Image(
                            bitmap = image,
                            contentDescription = "${puzzle.title} visual maths puzzle",
                            modifier = Modifier.fillMaxWidth().height(310.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text("This puzzle image could not be loaded.", Modifier.padding(28.dp), color = MaterialTheme.colorScheme.error)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
