package com.example.eduapp.data

import com.example.eduapp.model.Puzzle
import kotlinx.coroutines.flow.Flow

/** Keeps score rules and Room writes out of screens and ViewModels. */
class PuzzleRepository(private val dao: PuzzleProgressDao) {
    fun observeProgress(): Flow<List<PuzzleProgress>> = dao.observeAll()

    suspend fun recordWrongAttempt(puzzle: Puzzle, existing: PuzzleProgress?) {
        if (existing?.completedAt != null) return
        dao.save((existing ?: PuzzleProgress(puzzle.id)).copy(attempts = (existing?.attempts ?: 0) + 1))
    }

    suspend fun recordCompletion(puzzle: Puzzle, existing: PuzzleProgress?): Int {
        val attempts = (existing?.attempts ?: 0) + 1
        val score = scoreFor(attempts)
        dao.save(
            (existing ?: PuzzleProgress(puzzle.id)).copy(
                attempts = attempts,
                bestScore = maxOf(existing?.bestScore ?: 0, score),
                completedAt = System.currentTimeMillis()
            )
        )
        return score
    }

    suspend fun resetProgress() = dao.clear()

    companion object {
        /** A quick, accurate solution is rewarded without making later retries worthless. */
        fun scoreFor(attempts: Int): Int = when (attempts) {
            1 -> 150
            2 -> 110
            3 -> 80
            else -> 50
        }
    }
}
