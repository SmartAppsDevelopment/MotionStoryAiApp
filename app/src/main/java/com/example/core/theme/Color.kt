package com.example.core.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Brand Colors
val MotionPurplePrimary = Color(0xFF7E57FF)
val MotionPurpleDark = Color(0xFF6838EE)
val MotionPurpleLight = Color(0xFF9877FF)
val MotionPurpleBadge = Color(0xFF7A57FF)

// Background & Surface
val MotionBgDark = Color(0xFF0C0D12)
val MotionSurfaceDark = Color(0xFF161822)
val MotionSurfaceCard = Color(0xFF1B1D28)
val MotionSurfaceCardHover = Color(0xFF222533)
val MotionSurfaceVariant = Color(0xFF212330)
val MotionBorder = Color(0xFF2B2D3D)
val MotionBorderSubtle = Color(0xFF1F212E)

// Secondary & Accents
val MotionTeal = Color(0xFF2CD8A6)
val MotionAmber = Color(0xFFF5B942)
val MotionRed = Color(0xFFFF4B63)
val MotionBlue = Color(0xFF38BDF8)
val MotionPink = Color(0xFFF472B6)

// Text Colors
val MotionTextPrimary = Color(0xFFFFFFFF)
val MotionTextSecondary = Color(0xFF9597A8)
val MotionTextMuted = Color(0xFF63667B)

// Gradients
val MotionPrimaryGradient = Brush.horizontalGradient(
    colors = listOf(MotionPurpleDark, MotionPurplePrimary)
)
val MotionHeroGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF3B1E82), Color(0xFF201348))
)
val MotionCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF26194C), Color(0xFF151622))
)
val MotionProgressGradient = Brush.horizontalGradient(
    colors = listOf(MotionPurplePrimary, Color(0xFFC084FC))
)
