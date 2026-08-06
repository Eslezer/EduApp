package com.example.eduapp.model

/** A visual maths puzzle included with the app. */
data class Puzzle(
    val id: String,
    val chapter: Int,
    val title: String,
    val imagePath: String,
    val answer: String,
    val hint: String,
    val skill: String
)

/**
 * All level content lives in one reviewable place. Answers are strings so the same game
 * engine can later support number and word puzzles.
 */
object PuzzleCatalog {
    val puzzles = listOf(
        Puzzle("meadow-1", 1, "Berry Bunny", "1/level01_pic01_0.png", "0", "Work from the first row down. One strawberry stays on its own.", "Pattern substitution"),
        Puzzle("meadow-2", 1, "Fruit Counter", "1/level01_pic02_21.png", "21", "Find each fruit's value before combining the last row.", "Algebraic thinking"),
        Puzzle("meadow-3", 1, "Number Garden", "1/level01_pic03_15.png", "20", "Each row doubles the number on the left.", "Number patterns"),
        Puzzle("meadow-4", 1, "Spinner Squad", "1/level01_pic04_55.jpg", "55", "Check how many spinners appear in the final expression.", "Order of operations"),
        Puzzle("meadow-5", 1, "Animal Balances", "1/level01_pic05_6.jpg", "6", "Use the second equation to find the sheep first.", "Equations"),
        Puzzle("meadow-6", 1, "Cheese Clues", "1/level01_pic06_0.png", "0", "Use the second row to find one cat, then compare it with the first row.", "Careful observation"),
        Puzzle("orchard-1", 2, "Farm Friends", "2/level02_pic01_31.jpg", "31", "Work out the cat before solving the last line.", "Equations"),
        Puzzle("orchard-2", 2, "Summer Sum", "2/level02_pic02_26.jpg", "26", "The fruits change value between rows - count carefully.", "Visual maths"),
        Puzzle("orchard-3", 2, "Story Sprint", "2/level02_pic03_4.jpg", "4", "Follow the values in each picture group.", "Pattern reasoning"),
        Puzzle("orchard-4", 2, "Giraffe Grid", "2/level02_pic04_2.jpg", "2", "The final giraffe is not the same group as earlier rows.", "Order of operations"),
        Puzzle("orchard-5", 2, "Fruit Basket", "2/level02_pic05_35.jpg", "35", "Find banana, cherry and apple values one row at a time.", "Equations"),
        Puzzle("orchard-6", 2, "Hungry Challenge", "2/level02_pic06_63.jpg", "63", "The same food items reappear in different groups.", "Visual maths"),
        Puzzle("summit-1", 3, "Weight Watch", "3/level03_pic01_27.jpg", "27", "The final animal is shown alone - notice the unit label.", "Logic"),
        Puzzle("summit-2", 3, "Portrait Pairs", "3/level03_pic02_4.jpg", "4", "Solve the top row first, then substitute.", "Equations"),
        Puzzle("summit-3", 3, "Safari Signals", "3/level03_pic03_5.jpg", "5", "Start with the row that equals zero.", "Algebraic thinking"),
        Puzzle("summit-4", 3, "Colour Dash", "3/level03_pic04_24.jpg", "24", "Use the zero product before the last row.", "Number patterns"),
        Puzzle("summit-5", 3, "Road Ready", "3/level03_pic05_25.jpg", "25", "Use the car and scooter relationship before finding the motorcycle.", "Careful observation"),
        Puzzle("summit-6", 3, "Heart Quest", "3/level03_pic06_4.jpg", "4", "Use all three multiplication equations to find the green heart.", "Equations")
    )

    fun byId(id: String): Puzzle = puzzles.first { it.id == id }

    fun indexOf(id: String): Int = puzzles.indexOfFirst { it.id == id }
}

fun normaliseAnswer(answer: String): String = answer.trim().lowercase().replace(" ", "")
