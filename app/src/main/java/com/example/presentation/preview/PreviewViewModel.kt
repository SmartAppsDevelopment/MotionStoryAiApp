package com.example.presentation.preview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PreviewUiState(
    val isPlaying: Boolean = false,
    val isMuted: Boolean = false,
    val currentProgress: Float = 0.34f, // 00:06.1 / 00:18.0
    val currentTimeFormatted: String = "00:06.1",
    val totalTimeFormatted: String = "00:18.0",
    val effectSequenceText: String = "Photo 1 → Ken Burns → Fade → Photo 2"
)

class PreviewViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PreviewUiState())
    val uiState = _uiState.asStateFlow()

    fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun toggleMute() {
        _uiState.update { it.copy(isMuted = !it.isMuted) }
    }

    fun setProgress(progress: Float) {
        _uiState.update {
            val totalSec = 18.0f
            val curSec = progress * totalSec
            val formatted = String.format("%02d:%04.1f", (curSec / 60).toInt(), curSec % 60)
            it.copy(currentProgress = progress, currentTimeFormatted = formatted)
        }
    }
}
