package com.example.eduapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eduapp.navigation.AppDestination
import com.example.eduapp.viewmodel.GameViewModel

@Composable
fun SettingScreen(navController: NavHostController, viewModel: GameViewModel) {
    var showResetConfirmation by rememberSaveable { mutableStateOf(false) }

    PuzzleScaffold(navController, AppDestination.Settings) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("SETTINGS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text("Make it yours", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(Modifier.padding(18.dp)) {
                    Text("Puzzle Trail", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("A visual maths adventure using 18 supplied puzzle images. Answers are checked locally and progress stays on this device.")
                }
            }
            Text("DATA", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Start a fresh trail", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("This permanently removes solved-puzzle history and points from this device.")
                    Button(onClick = { showResetConfirmation = true }) { Text("Reset progress") }
                }
            }
        }
    }

    if (showResetConfirmation) {
        AlertDialog(
            onDismissRequest = { showResetConfirmation = false },
            title = { Text("Reset your trail?") },
            text = { Text("Your puzzle progress and all points will be permanently removed. This cannot be undone.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.resetProgress()
                    showResetConfirmation = false
                    navController.navigate(AppDestination.Home) {
                        popUpTo(AppDestination.Home) { inclusive = true }
                    }
                }) { Text("Reset permanently") }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmation = false }) { Text("Keep progress") }
            }
        )
    }
}
