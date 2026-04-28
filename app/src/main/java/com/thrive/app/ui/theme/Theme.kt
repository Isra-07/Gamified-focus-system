package com.thrive.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ThriveDarkColors = darkColorScheme(
    primary = Violet,
    onPrimary = TextPrimary,
    secondary = CyanAccent,
    onSecondary = Night,
    tertiary = EmeraldAccent,
    background = Night,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = DividerDark
)

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ThriveDarkColors,
        typography = Typography,
        content = content
    )
}
