package com.thrive.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFF7C6FCD)
val Green40 = Color(0xFF4ADE80)
val DarkBg = Color(0xFF0F0F13)

private val DarkColors = darkColorScheme(
    primary = Purple80,
    secondary = Green40,
    background = DarkBg,
    surface = Color(0xFF1A1A24),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun ThriveTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColors, content = content)
}
