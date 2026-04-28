package com.thrive.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Night = Color(0xFF0A0B14)
val Midnight = Color(0xFF101222)
val SurfaceDark = Color(0xFF171927)
val SurfaceRaised = Color(0xFF212438)
val SurfaceCard = Color(0xFF151826)
val TextPrimary = Color(0xFFF8F8FC)
val TextSecondary = Color(0xFF9A9DB3)
val DividerDark = Color(0xFF32364A)
val Leaf = Color(0xFF4DD97A)
val Aqua = Color(0xFF5AB8FF)
val Sun = Color(0xFFF3C349)
val Coral = Color(0xFFFF7A7A)
val Violet = Color(0xFF7B4DFF)
val VioletBright = Color(0xFFA07CFF)
val PinkGlow = Color(0xFFF05CCB)

private val ThriveColors = darkColorScheme(
    primary = VioletBright,
    onPrimary = TextPrimary,
    secondary = Aqua,
    onSecondary = Night,
    tertiary = Leaf,
    background = Night,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = DividerDark,
    error = Coral
)

private val ThriveTypography = Typography(
    displaySmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.ExtraBold, fontSize = 42.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.ExtraBold, fontSize = 34.sp),
    headlineSmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 24.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 20.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
)

@Composable
fun ThriveTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = ThriveColors,
        typography = ThriveTypography,
        content = content
    )
}
