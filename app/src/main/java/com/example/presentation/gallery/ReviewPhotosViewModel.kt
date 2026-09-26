package com.example.presentation.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.PhotoItem
import com.example.data.repository.FakePhotoRepository
import com.example.data.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReviewPhotosUiState(
    val photos: List<PhotoItem> = emptyList(),
    val totalDurationFormatted: String = "00:18 total"
)

class ReviewPhotosViewModel(
    private val photoRepository: PhotoRepository = FakePhotoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewPhotosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            photoRepository.getSelectedPhotos().collect { photos ->
                val totalSec = photos.sumOf { it.durationSeconds.toDouble() }
                val minutes = (totalSec / 60).toInt()
                val seconds = (totalSec % 60).toInt()
                val totalStr = String.format("%02d:%02d total", minutes, seconds)
                _uiState.update { it.copy(photos = photos, totalDurationFormatted = totalStr) }
            }
        }
    }

    fun removePhoto(photoId: String) {
        viewModelScope.launch {
            photoRepository.removeSelectedPhoto(photoId)
        }
    }

    fun reorderPhotos(photos: List<PhotoItem>) {
        viewModelScope.launch {
            photoRepository.reorderSelectedPhotos(photos)
        }
    }
}
