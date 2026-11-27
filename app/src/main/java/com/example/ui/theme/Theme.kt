package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val GorphinDarkColorScheme = darkColorScheme(
    primary = CobaltPrimary,
    onPrimary = Color.White,
    primaryContainer = CobaltContainer,
    onPrimaryContainer = CobaltOnContainer,
    secondary = CyanSecondary,
    onSecondary = Color(0xFF002028),
    secondaryContainer = CyanContainer,
    onSecondaryContainer = CyanOnContainer,
    tertiary = ThinkViolet,
    onTertiary = Color.White,
    tertiaryContainer = ThinkVioletContainer,
    onTertiaryContainer = ThinkVioletText,
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = ObsidianSurface,
    onSurface = TextPrimary,
    surfaceVariant = ObsidianSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    surfaceContainerLowest = ObsidianCodeBg,
    surfaceContainerLow = ObsidianSidebar,
    surfaceContainer = ObsidianSurface,
    surfaceContainerHigh = ObsidianSurfaceElevated,
    surfaceContainerHighest = Color(0xFF253147),
    outline = ObsidianBorder,
    outlineVariant = Color(0xFF1F2A3E),
    error = StatusError,
    onError = Color.White,
    errorContainer = StatusErrorContainer,
    onErrorContainer = StatusErrorText,
)

val GorphinShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

@Composable
fun GorphinTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GorphinDarkColorScheme,
        typography = GorphinTypography,
        shapes = GorphinShapes,
        content = content
    )
}
