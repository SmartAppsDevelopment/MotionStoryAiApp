package com.example.presentation.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val theme: String = "System",
    val defaultPhotoDuration: String = "1.5 sec",
    val defaultTransition: String = "Fade",
    val defaultAspectRatio: String = "9:16",
    val defaultResolution: String = "1080p",
    val defaultFps: String = "30 FPS",
    val videoQuality: String = "High",
    val defaultMusicVolume: String = "70%",
    val cacheSize: String = "248 MB",
    val storageUsage: String = "1.8 GB",
    val appVersion: String = "v1.4.0",
    val cacheClearedMessage: String? = null
)

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    fun clearCache() {
        _uiState.update { it.copy(cacheSize = "0 MB", cacheClearedMessage = "Cache cleared") }
    }

    fun dismissMessage() {
        _uiState.update { it.copy(cacheClearedMessage = null) }
    }
}
