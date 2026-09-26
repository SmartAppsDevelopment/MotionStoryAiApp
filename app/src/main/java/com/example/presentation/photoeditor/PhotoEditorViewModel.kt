package com.example.presentation.photoeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.FilterType
import com.example.data.model.PhotoItem
import com.example.data.repository.FakePhotoRepository
import com.example.data.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoEditorViewModel(
    private val photoRepository: PhotoRepository = FakePhotoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoEditorUiState())
    val uiState = _uiState.asStateFlow()

    fun loadPhoto(photoId: String) {
        viewModelScope.launch {
            photoRepository.getSelectedPhotos().collect { items ->
                val match = items.find { it.id == photoId } ?: items.firstOrNull()
                if (match != null) {
                    _uiState.update {
                        it.copy(
                            photo = match,
                            brightness = match.adjustment.brightness,
                            contrast = match.adjustment.contrast,
                            saturation = match.adjustment.saturation,
                            exposure = match.adjustment.exposure,
                            highlights = match.adjustment.highlights,
                            shadows = match.adjustment.shadows,
                            selectedCropRatio = match.adjustment.cropRatio,
                            straightenDeg = match.adjustment.straightenDeg,
                            selectedFilter = match.adjustment.filter,
                            filterIntensity = match.adjustment.filterIntensity
                        )
                    }
                }
            }
        }
    }

    fun selectTab(tab: PhotoEditorTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun selectAdjustTool(tool: AdjustTool) {
        _uiState.update { it.copy(selectedAdjustTool = tool) }
    }

    fun updateCurrentAdjustValue(value: Float) {
        _uiState.update {
            when (it.selectedAdjustTool) {
                AdjustTool.BRIGHTNESS -> it.copy(brightness = value)
                AdjustTool.CONTRAST -> it.copy(contrast = value)
                AdjustTool.SATURATION -> it.copy(saturation = value)
                AdjustTool.EXPOSURE -> it.copy(exposure = value)
                AdjustTool.HIGHLIGHTS -> it.copy(highlights = value)
                AdjustTool.SHADOWS -> it.copy(shadows = value)
            }
        }
    }

    fun resetAdjustments() {
        _uiState.update {
            it.copy(
                brightness = 0f,
                contrast = 0f,
                saturation = 0f,
                exposure = 0f,
                highlights = 0f,
                shadows = 0f
            )
        }
    }

    fun selectCropRatio(ratio: String) {
        _uiState.update { it.copy(selectedCropRatio = ratio) }
    }

    fun setStraightenDeg(deg: Float) {
        _uiState.update { it.copy(straightenDeg = deg) }
    }

    fun selectFilter(filter: FilterType) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun setFilterIntensity(intensity: Float) {
        _uiState.update { it.copy(filterIntensity = intensity) }
    }

    fun applyChanges(onComplete: () -> Unit) {
        val currentPhoto = _uiState.value.photo ?: return
        val updatedAdjustment = currentPhoto.adjustment.copy(
            brightness = _uiState.value.brightness,
            contrast = _uiState.value.contrast,
            saturation = _uiState.value.saturation,
            exposure = _uiState.value.exposure,
            highlights = _uiState.value.highlights,
            shadows = _uiState.value.shadows,
            cropRatio = _uiState.value.selectedCropRatio,
            straightenDeg = _uiState.value.straightenDeg,
            filter = _uiState.value.selectedFilter,
            filterIntensity = _uiState.value.filterIntensity
        )
        viewModelScope.launch {
            photoRepository.updatePhoto(currentPhoto.copy(adjustment = updatedAdjustment))
            onComplete()
        }
    }
}
