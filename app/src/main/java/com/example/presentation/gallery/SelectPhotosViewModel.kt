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

class SelectPhotosViewModel(
    private val photoRepository: PhotoRepository = FakePhotoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectPhotosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            photoRepository.getAvailablePhotos().collect { photos ->
                _uiState.update { it.copy(availablePhotos = photos) }
            }
        }
        viewModelScope.launch {
            photoRepository.getSelectedPhotos().collect { selected ->
                _uiState.update { it.copy(selectedPhotos = selected) }
            }
        }
    }

    fun selectTab(tab: Int) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun togglePhoto(photoId: String) {
        viewModelScope.launch {
            photoRepository.togglePhotoSelection(photoId)
        }
    }

    fun removePhoto(photoId: String) {
        viewModelScope.launch {
            photoRepository.removeSelectedPhoto(photoId)
        }
    }
}
