package com.example.eduapp

import com.example.eduapp.data.PuzzleRepository
import com.example.eduapp.model.PuzzleCatalog
import com.example.eduapp.model.normaliseAnswer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PuzzleRulesTest {
    @Test
    fun `catalogue has six puzzles in every chapter`() {
        assertEquals(18, PuzzleCatalog.puzzles.size)
        assertTrue((1..3).all { chapter -> PuzzleCatalog.puzzles.count { it.chapter == chapter } == 6 })
    }

    @Test
    fun `answers ignore surrounding spaces`() {
        assertEquals("21", normaliseAnswer(" 21 "))
    }

    @Test
    fun `first try receives the highest score`() {
        assertEquals(150, PuzzleRepository.scoreFor(1))
        assertEquals(110, PuzzleRepository.scoreFor(2))
        assertEquals(50, PuzzleRepository.scoreFor(9))
    }
}
