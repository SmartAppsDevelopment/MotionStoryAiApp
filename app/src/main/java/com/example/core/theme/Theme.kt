package com.example.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val MotionDarkColorScheme = darkColorScheme(
    primary = MotionPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = MotionPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = MotionPurpleLight,
    onSecondary = Color.White,
    secondaryContainer = MotionSurfaceVariant,
    onSecondaryContainer = MotionTextPrimary,
    tertiary = MotionTeal,
    onTertiary = Color.Black,
    background = MotionBgDark,
    onBackground = MotionTextPrimary,
    surface = MotionSurfaceDark,
    onSurface = MotionTextPrimary,
    surfaceVariant = MotionSurfaceVariant,
    onSurfaceVariant = MotionTextSecondary,
    outline = MotionBorder,
    outlineVariant = MotionBorderSubtle,
    error = MotionRed,
    onError = Color.White
)

@Composable
fun MotionStoryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MotionDarkColorScheme,
        typography = MotionTypography,
        shapes = MotionShapes,
        content = content
    )
}
