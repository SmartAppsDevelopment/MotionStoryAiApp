package com.example.presentation.texteditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.PhotoItem
import com.example.data.model.TextOverlay
import com.example.data.repository.FakePhotoRepository
import com.example.data.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TextEditorUiState(
    val photo: PhotoItem? = null,
    val text: String = "Wander often",
    val selectedFont: String = "Inter",
    val fontSizeSp: Float = 24f,
    val isBold: Boolean = true,
    val isItalic: Boolean = false,
    val hasBackground: Boolean = true,
    val selectedAnimation: String = "Fade"
)

class TextEditorViewModel(
    private val photoRepository: PhotoRepository = FakePhotoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TextEditorUiState())
    val uiState = _uiState.asStateFlow()

    fun loadPhoto(photoId: String) {
        viewModelScope.launch {
            photoRepository.getSelectedPhotos().collect { photos ->
                val match = photos.find { it.id == photoId } ?: photos.firstOrNull()
                _uiState.update {
                    it.copy(
                        photo = match,
                        text = match?.textOverlay?.text ?: "Wander often"
                    )
                }
            }
        }
    }

    fun updateText(newText: String) {
        _uiState.update { it.copy(text = newText) }
    }

    fun selectAnimation(animation: String) {
        _uiState.update { it.copy(selectedAnimation = animation) }
    }

    fun toggleBold() {
        _uiState.update { it.copy(isBold = !it.isBold) }
    }

    fun toggleItalic() {
        _uiState.update { it.copy(isItalic = !it.isItalic) }
    }

    fun toggleBackground() {
        _uiState.update { it.copy(hasBackground = !it.hasBackground) }
    }

    fun applyText(onComplete: () -> Unit) {
        val currentPhoto = _uiState.value.photo ?: return
        val overlay = TextOverlay(
            text = _uiState.value.text,
            isBold = _uiState.value.isBold,
            isItalic = _uiState.value.isItalic,
            hasBackground = _uiState.value.hasBackground,
            animation = _uiState.value.selectedAnimation
        )
        viewModelScope.launch {
            photoRepository.updatePhoto(currentPhoto.copy(textOverlay = overlay))
            onComplete()
        }
    }
}
