package com.example.presentation.editor

import com.example.data.model.AnimationCategory
import com.example.data.model.AnimationType
import com.example.data.model.MusicTrack
import com.example.data.model.PhotoItem
import com.example.data.model.TransitionType

enum class EditorSheet {
    NONE,
    ANIMATION,
    TRANSITION,
    DURATION,
    MUSIC
}

data class EditorUiState(
    val projectTitle: String = "Summer Trip",
    val photos: List<PhotoItem> = emptyList(),
    val selectedPhotoIndex: Int = 0,
    val isPlaying: Boolean = false,
    val currentTimeSec: Float = 6.0f,
    val totalTimeSec: Float = 18.0f,
    val selectedMusic: MusicTrack? = null,
    val activeSheet: EditorSheet = EditorSheet.NONE,
    // Sheet temporary states
    val tempAnimationCategory: AnimationCategory = AnimationCategory.MOTION,
    val tempAnimationType: AnimationType = AnimationType.KEN_BURNS,
    val tempTransitionType: TransitionType = TransitionType.FADE,
    val tempTransitionDuration: Float = 0.6f,
    val tempDurationSeconds: Float = 1.5f,
    val applyDurationToAll: Boolean = false
) {
    val selectedPhoto: PhotoItem?
        get() = photos.getOrNull(selectedPhotoIndex)
}
