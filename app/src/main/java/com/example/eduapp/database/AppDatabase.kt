package com.example.eduapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eduapp.data.PuzzleProgress
import com.example.eduapp.data.PuzzleProgressDao

@Database(entities = [User::class, PuzzleProgress::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun puzzleProgressDao(): PuzzleProgressDao
}
