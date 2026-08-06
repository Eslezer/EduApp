package com.example.eduapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TrailPinkLight,
    secondary = TrailVioletLight,
    tertiary = TrailGold,
    background = TrailNight,
    surface = TrailNight
)

private val LightColorScheme = lightColorScheme(
    primary = TrailPink,
    secondary = TrailViolet,
    tertiary = TrailGold,
    background = TrailCream,
    surface = TrailCream
)

@Composable
fun EduAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
