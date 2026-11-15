package com.damaskitchen.studio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ----- Brand palette --------------------------------------------------------
val DamasChampagneGold = Color(0xFFD4AF37)
val DamasWarmCream = Color(0xFFEAE3DC)
val DamasLightOak = Color(0xFFB89F86)
val DamasRoseCappuccino = Color(0xFFC8A290)
val DamasAnthracite = Color(0xFF2B2D30)
val DamasObsidian = Color(0xFF141518)
val DamasSlateSurface = Color(0xFF1E2024)
val DamasSlateContainer = Color(0xFF272A30)
val DamasLedWarm3000K = Color(0xFFFFD580)

// ----- Light theme -----------------------------------------------------------
private val LightColors = lightColorScheme(
    primary = DamasChampagneGold,
    onPrimary = DamasObsidian,
    primaryContainer = Color(0xFF473A12),
    onPrimaryContainer = Color(0xFFFBE7A2),

    secondary = DamasLightOak,
    onSecondary = DamasObsidian,
    secondaryContainer = Color(0xFF3E2F15),
    onSecondaryContainer = Color(0xFFFFE5B5),

    tertiary = DamasRoseCappuccino,
    onTertiary = DamasObsidian,
    tertiaryContainer = Color(0xFF45362A),
    onTertiaryContainer = Color(0xFFFFE0C2),

    background = DamasWarmCream,
    onBackground = DamasObsidian,
    surface = DamasWarmCream,
    onSurface = DamasObsidian,

    surfaceVariant = DamasSlateContainer,
    onSurfaceVariant = DamasSlateSurface,
    outline = DamasObsidian
)

// ----- Dark theme ------------------------------------------------------------
private val DarkColors = darkColorScheme(
    primary = DamasChampagneGold,
    onPrimary = DamasObsidian,
    primaryContainer = Color(0xFF5C4A1B),
    onPrimaryContainer = Color(0xFFFBE7A2),

    secondary = DamasLightOak,
    onSecondary = DamasObsidian,
    secondaryContainer = Color(0xFF4B3819),
    onSecondaryContainer = Color(0xFFFFE5B5),

    tertiary = DamasRoseCappuccino,
    onTertiary = DamasObsidian,
    tertiaryContainer = Color(0xFF5A442E),
    onTertiaryContainer = Color(0xFFFFE0C2),

    background = DamasSlateSurface,
    onBackground = DamasSlateContainer,
    surface = DamasSlateSurface,
    onSurface = DamasSlateContainer,

    surfaceVariant = DamasSlateContainer,
    onSurfaceVariant = DamasSlateSurface,
    outline = DamasObsidian
)

// ----- Shapes ----------------------------------------------------------------
val DamasShapes = androidx.compose.material3.Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
)

// ----- Theme entry point -----------------------------------------------------
@Composable
fun DamasKitchenTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (useDarkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(),
        shapes = DamasShapes,
        content = content
    )
}
