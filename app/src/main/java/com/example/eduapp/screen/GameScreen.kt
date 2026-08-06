package com.example.eduapp.screen

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.helper.rememberAssetImage
import com.example.eduapp.model.PuzzleCatalog
import com.example.eduapp.model.normaliseAnswer
import com.example.eduapp.navigation.AppDestination
import com.example.eduapp.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(puzzleId: String, navController: NavHostController, viewModel: GameViewModel) {
    val puzzle = remember(puzzleId) { PuzzleCatalog.byId(puzzleId) }
    val image = rememberAssetImage(puzzle.imagePath)
    val position = PuzzleCatalog.indexOf(puzzle.id) + 1
    var answer by rememberSaveable(puzzleId) { mutableStateOf("") }
    var feedback by rememberSaveable(puzzleId) { mutableStateOf<String?>(null) }
    var showHint by rememberSaveable(puzzleId) { mutableStateOf(false) }
    var answerRevealed by rememberSaveable(puzzleId) { mutableStateOf(false) }
    var completionScore by rememberSaveable(puzzleId) { mutableStateOf<Int?>(null) }
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 45) }
    DisposableEffect(toneGenerator) { onDispose { toneGenerator.release() } }

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
                if (showHint) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                        Text("Hint: ${puzzle.hint}", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onTertiaryContainer)
                    }
                }
                OutlinedTextField(
                    value = answer,
                    onValueChange = { entered ->
                        answer = entered.filter { it.isDigit() || it == '.' }.take(8)
                        feedback = null
                    },
                    label = { Text("Your answer") },
                    supportingText = { feedback?.let { Text(it) } },
                    isError = feedback?.startsWith("Not") == true || feedback?.startsWith("Enter") == true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        when {
                            answer.isBlank() -> feedback = "Enter a number before checking."
                            normaliseAnswer(answer) == normaliseAnswer(puzzle.answer) -> {
                                viewModel.recordCompletion(puzzle, awardPoints = !answerRevealed) { score ->
                                    toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 150)
                                    completionScore = score
                                }
                            }
                            else -> {
                                viewModel.recordWrongAttempt(puzzle)
                                feedback = "Not quite - check each row and try again."
                            }
                        }
                    }) { Text("Check answer") }
                    TextButton(onClick = { showHint = !showHint }) { Text(if (showHint) "Hide hint" else "Show hint") }
                }
                androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = {
                        viewModel.recordWrongAttempt(puzzle)
                        answer = puzzle.answer
                        answerRevealed = true
                        showHint = false
                        feedback = "Solution revealed. You can continue, but this puzzle will earn 0 points."
                    }) { Text("Reveal solution") }
                    TextButton(onClick = {
                        navController.navigate(AppDestination.game(PuzzleCatalog.puzzles.random().id))
                    }) { Text("Random puzzle") }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    completionScore?.let { earnedScore ->
        val nextPuzzle = PuzzleCatalog.puzzles.getOrNull(position)
        AlertDialog(
            onDismissRequest = { navController.navigate(AppDestination.Levels) { launchSingleTop = true } },
            title = { Text("Trail star earned!") },
            text = {
                Text(
                    if (earnedScore == 0) "Puzzle completed. Revealed solutions earn 0 points."
                    else "Great reasoning - you earned $earnedScore points for ${puzzle.title}."
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (nextPuzzle == null) {
                        navController.navigate(AppDestination.Scores) { launchSingleTop = true }
                    } else {
                        navController.navigate(AppDestination.game(nextPuzzle.id))
                    }
                }) { Text(if (nextPuzzle == null) "See progress" else "Next puzzle") }
            },
            dismissButton = {
                TextButton(onClick = { navController.navigate(AppDestination.Levels) { launchSingleTop = true } }) {
                    Text("Puzzle map")
                }
            }
        )
    }
}
