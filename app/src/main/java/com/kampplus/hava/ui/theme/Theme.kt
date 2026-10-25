package com.kampplus.hava.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF42652F), onPrimary = Color(0xFFFFF8E5),
    primaryContainer = Color(0xFFDDE7B4), onPrimaryContainer = Color(0xFF293C1C),
    secondary = Color(0xFF87512C), onSecondary = Color(0xFFFFF4D8),
    secondaryContainer = Color(0xFFF2D69B), onSecondaryContainer = Color(0xFF54341D),
    tertiary = Color(0xFF427784),
    background = Color(0xFFF6EDD6), onBackground = Color(0xFF3D2B20),
    surface = Color(0xFFFFF7E2), onSurface = Color(0xFF3D2B20),
    surfaceVariant = Color(0xFFF0DEB7), onSurfaceVariant = Color(0xFF715B43),
    surfaceContainer = Color(0xFFECD9AE), surfaceContainerHigh = Color(0xFFF0DEB7),
    surfaceContainerHighest = Color(0xFFF0DEB7), surfaceContainerLow = Color(0xFFFBF1D9),
    surfaceContainerLowest = Color(0xFFFFF7E2),
    outline = Color(0xFF947046), outlineVariant = Color(0xFFD1B483),
    error = Color(0xFFA4372B)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBAD58A), onPrimary = Color(0xFF26391D),
    primaryContainer = Color(0xFF3B5130), onPrimaryContainer = Color(0xFFE0EDBE),
    secondary = Color(0xFFEBC08B), onSecondary = Color(0xFF3C281B),
    secondaryContainer = Color(0xFF60472F), onSecondaryContainer = Color(0xFFFFE5B9),
    tertiary = Color(0xFF94C7CE),
    background = Color(0xFF1D2528), onBackground = Color(0xFFF1E4C8),
    surface = Color(0xFF293133), onSurface = Color(0xFFF1E4C8),
    surfaceVariant = Color(0xFF35403B), onSurfaceVariant = Color(0xFFCABD9F),
    surfaceContainer = Color(0xFF303A36), surfaceContainerHigh = Color(0xFF35403B),
    surfaceContainerHighest = Color(0xFF35403B), surfaceContainerLow = Color(0xFF25302E),
    surfaceContainerLowest = Color(0xFF1D2528),
    outline = Color(0xFFAD8B5C), outlineVariant = Color(0xFF655A44),
    error = Color(0xFFFFB4A1)
)

@Composable
fun HavaTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = HavaTypography,
        shapes = Shapes(
            extraSmall = CutCornerShape(2.dp),
            small = CutCornerShape(4.dp),
            medium = CutCornerShape(6.dp),
            large = CutCornerShape(8.dp),
            extraLarge = CutCornerShape(10.dp)
        ),
        content = content
    )
}
