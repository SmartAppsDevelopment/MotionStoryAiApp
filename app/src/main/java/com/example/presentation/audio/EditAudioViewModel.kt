package com.example.presentation.audio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MusicTrack
import com.example.data.repository.FakeMusicRepository
import com.example.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditAudioUiState(
    val track: MusicTrack? = null,
    val isPlaying: Boolean = false,
    val startTrimSec: Float = 12.4f,
    val endTrimSec: Float = 30.4f,
    val fadeIn: Boolean = true,
    val fadeOut: Boolean = true,
    val musicVolume: Float = 72f,
    val originalAudioVolume: Float = 0f
)

class EditAudioViewModel(
    private val musicRepository: MusicRepository = FakeMusicRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditAudioUiState())
    val uiState = _uiState.asStateFlow()

    fun loadTrack(trackId: String) {
        viewModelScope.launch {
            musicRepository.getTracks().collect { list ->
                val track = list.find { it.id == trackId } ?: list.firstOrNull()
                if (track != null) {
                    _uiState.update {
                        it.copy(
                            track = track,
                            startTrimSec = track.startTrimSec,
                            endTrimSec = track.endTrimSec,
                            fadeIn = track.fadeIn,
                            fadeOut = track.fadeOut,
                            musicVolume = track.musicVolume,
                            originalAudioVolume = track.originalAudioVolume
                        )
                    }
                }
            }
        }
    }

    fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun setFadeIn(enabled: Boolean) {
        _uiState.update { it.copy(fadeIn = enabled) }
    }

    fun setFadeOut(enabled: Boolean) {
        _uiState.update { it.copy(fadeOut = enabled) }
    }

    fun setMusicVolume(volume: Float) {
        _uiState.update { it.copy(musicVolume = volume) }
    }

    fun setOriginalAudioVolume(volume: Float) {
        _uiState.update { it.copy(originalAudioVolume = volume) }
    }

    fun applySettings(onComplete: () -> Unit) {
        val currentTrack = _uiState.value.track ?: return
        viewModelScope.launch {
            musicRepository.updateTrackSettings(
                trackId = currentTrack.id,
                startSec = _uiState.value.startTrimSec,
                endSec = _uiState.value.endTrimSec,
                fadeIn = _uiState.value.fadeIn,
                fadeOut = _uiState.value.fadeOut,
                volume = _uiState.value.musicVolume,
                originalAudioVolume = _uiState.value.originalAudioVolume
            )
            onComplete()
        }
    }
}
