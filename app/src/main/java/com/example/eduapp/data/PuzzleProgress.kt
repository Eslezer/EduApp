package com.example.eduapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Persistent result for one Puzzle Trail challenge. */
@Entity(tableName = "puzzle_progress")
data class PuzzleProgress(
    @PrimaryKey val puzzleId: String,
    val attempts: Int = 0,
    val bestScore: Int = 0,
    val completedAt: Long? = null
)
