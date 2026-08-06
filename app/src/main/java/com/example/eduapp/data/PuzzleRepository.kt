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

    suspend fun recordCompletion(
        puzzle: Puzzle,
        existing: PuzzleProgress?,
        awardPoints: Boolean = true
    ): Int {
        val attempts = (existing?.attempts ?: 0) + 1
        val score = scoreFor(attempts, awardPoints)
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
        /** Starts at 150 points and removes 10 for every wrong attempt. */
        fun scoreFor(attempts: Int, awardPoints: Boolean = true): Int {
            if (!awardPoints) return 0
            val wrongAttempts = (attempts - 1).coerceAtLeast(0)
            return (150 - wrongAttempts * 10).coerceAtLeast(0)
        }
    }
}
