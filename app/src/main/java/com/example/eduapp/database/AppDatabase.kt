package com.example.eduapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.eduapp.data.PuzzleProgress
import com.example.eduapp.data.PuzzleProgressDao

@Database(entities = [User::class, PuzzleProgress::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun puzzleProgressDao(): PuzzleProgressDao

    companion object {
        /**
         * Version 1 existed in two development builds: the supplied template had only
         * `users`, while an early Puzzle Trail build had only `puzzle_progress`.
         * Creating both missing tables makes either installed version upgrade safely.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `users` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `username` TEXT NOT NULL,
                        `level` TEXT NOT NULL,
                        `score` INTEGER NOT NULL,
                        `duration` INTEGER NOT NULL,
                        `date` INTEGER NOT NULL
                    )""".trimIndent()
                )
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `puzzle_progress` (
                        `puzzleId` TEXT NOT NULL,
                        `attempts` INTEGER NOT NULL,
                        `bestScore` INTEGER NOT NULL,
                        `completedAt` INTEGER,
                        PRIMARY KEY(`puzzleId`)
                    )""".trimIndent()
                )
            }
        }
    }
}
