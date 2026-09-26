package com.example.presentation.photoeditor

import com.example.data.model.FilterType
import com.example.data.model.PhotoItem

enum class PhotoEditorTab {
    ADJUST,
    TRANSFORM,
    FILTERS
}

enum class AdjustTool(val title: String) {
    BRIGHTNESS("Brightness"),
    CONTRAST("Contrast"),
    SATURATION("Saturation"),
    EXPOSURE("Exposure"),
    HIGHLIGHTS("Highlights"),
    SHADOWS("Shadows")
}

data class PhotoEditorUiState(
    val photo: PhotoItem? = null,
    val activeTab: PhotoEditorTab = PhotoEditorTab.ADJUST,
    val selectedAdjustTool: AdjustTool = AdjustTool.BRIGHTNESS,
    val brightness: Float = 18f,
    val contrast: Float = 0f,
    val saturation: Float = 0f,
    val exposure: Float = 0f,
    val highlights: Float = 0f,
    val shadows: Float = 0f,
    val selectedCropRatio: String = "9:16",
    val straightenDeg: Float = 0f,
    val selectedFilter: FilterType = FilterType.CINEMATIC,
    val filterIntensity: Float = 70f
)
