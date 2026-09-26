package com.example.data.model

data class PhotoAdjustment(
    val brightness: Float = 18f,
    val contrast: Float = 0f,
    val saturation: Float = 0f,
    val exposure: Float = 0f,
    val highlights: Float = 0f,
    val shadows: Float = 0f,
    val straightenDeg: Float = 0f,
    val cropRatio: String = "9:16",
    val filter: FilterType = FilterType.CINEMATIC,
    val filterIntensity: Float = 70f
)

data class TextOverlay(
    val text: String = "Wander often",
    val font: String = "Inter",
    val fontSizeSp: Float = 24f,
    val colorHex: String = "#FFFFFF",
    val isBold: Boolean = true,
    val isItalic: Boolean = false,
    val hasBackground: Boolean = true,
    val opacity: Float = 1.0f,
    val animation: String = "Fade in"
)

data class PhotoItem(
    val id: String,
    val drawableResId: Int,
    val title: String,
    val order: Int,
    val durationSeconds: Float = 1.5f,
    val animation: AnimationType = AnimationType.KEN_BURNS,
    val transition: TransitionType = TransitionType.FADE,
    val transitionDuration: Float = 0.6f,
    val adjustment: PhotoAdjustment = PhotoAdjustment(),
    val textOverlay: TextOverlay? = null
)
