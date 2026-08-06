package com.example.eduapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.eduapp.data.PuzzleProgress
import com.example.eduapp.data.PuzzleRepository
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.model.Puzzle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Owns game progress and keeps composables free of database work. */
class GameViewModel(private val repository: PuzzleRepository) : ViewModel() {
    val progress: StateFlow<List<PuzzleProgress>> = repository.observeProgress().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun progressFor(puzzleId: String): PuzzleProgress? =
        progress.value.firstOrNull { it.puzzleId == puzzleId }

    fun recordWrongAttempt(puzzle: Puzzle) = viewModelScope.launch {
        repository.recordWrongAttempt(puzzle, progressFor(puzzle.id))
    }

    fun recordCompletion(puzzle: Puzzle, onRecorded: (Int) -> Unit) = viewModelScope.launch {
        onRecorded(repository.recordCompletion(puzzle, progressFor(puzzle.id)))
    }

    fun resetProgress() = viewModelScope.launch { repository.resetProgress() }
}

/** Creates the Room-backed ViewModel from the existing application context. */
class GameViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val appContext = context.applicationContext

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = Room.databaseBuilder(appContext, AppDatabase::class.java, "puzzle-trail.db")
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
        return GameViewModel(PuzzleRepository(database.puzzleProgressDao())) as T
    }
}
