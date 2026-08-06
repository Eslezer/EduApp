package com.example.eduapp.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/** Room queries for the level map, score journal, and progress reset. */
@Dao
interface PuzzleProgressDao {
    @Query("SELECT * FROM puzzle_progress ORDER BY completedAt DESC")
    fun observeAll(): Flow<List<PuzzleProgress>>

    @Upsert
    suspend fun save(progress: PuzzleProgress)

    @Query("DELETE FROM puzzle_progress")
    suspend fun clear()
}
