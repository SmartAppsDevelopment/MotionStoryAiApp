package com.example.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AnimationCategory
import com.example.data.model.AnimationType
import com.example.data.model.MusicTrack
import com.example.data.model.PhotoItem
import com.example.data.model.TransitionType
import com.example.data.repository.FakeMusicRepository
import com.example.data.repository.FakePhotoRepository
import com.example.data.repository.FakeProjectRepository
import com.example.data.repository.MusicRepository
import com.example.data.repository.PhotoRepository
import com.example.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditorViewModel(
    private val photoRepository: PhotoRepository = FakePhotoRepository(),
    private val musicRepository: MusicRepository = FakeMusicRepository(),
    private val projectRepository: ProjectRepository = FakeProjectRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            photoRepository.getSelectedPhotos().collect { items ->
                val totalDuration = items.sumOf { it.durationSeconds.toDouble() }.toFloat()
                _uiState.update {
                    it.copy(
                        photos = items,
                        totalTimeSec = if (totalDuration > 0f) totalDuration else 18.0f
                    )
                }
            }
        }
        viewModelScope.launch {
            musicRepository.getSelectedTrack().collect { track ->
                _uiState.update { it.copy(selectedMusic = track) }
            }
        }
    }

    fun loadProject(projectId: String?) {
        if (projectId != null) {
            viewModelScope.launch {
                val proj = projectRepository.getProjectById(projectId)
                if (proj != null) {
                    _uiState.update { it.copy(projectTitle = proj.title) }
                }
            }
        }
    }

    fun selectPhoto(index: Int) {
        if (index in _uiState.value.photos.indices) {
            val photo = _uiState.value.photos[index]
            _uiState.update {
                it.copy(
                    selectedPhotoIndex = index,
                    tempAnimationType = photo.animation,
                    tempTransitionType = photo.transition,
                    tempDurationSeconds = photo.durationSeconds
                )
            }
        }
    }

    fun nextPhoto() {
        val next = (_uiState.value.selectedPhotoIndex + 1) % _uiState.value.photos.size.coerceAtLeast(1)
        selectPhoto(next)
    }

    fun previousPhoto() {
        val prev = if (_uiState.value.selectedPhotoIndex > 0) {
            _uiState.value.selectedPhotoIndex - 1
        } else {
            (_uiState.value.photos.size - 1).coerceAtLeast(0)
        }
        selectPhoto(prev)
    }

    fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun openSheet(sheet: EditorSheet) {
        val currentPhoto = _uiState.value.selectedPhoto
        _uiState.update {
            it.copy(
                activeSheet = sheet,
                tempAnimationType = currentPhoto?.animation ?: AnimationType.KEN_BURNS,
                tempTransitionType = currentPhoto?.transition ?: TransitionType.FADE,
                tempTransitionDuration = currentPhoto?.transitionDuration ?: 0.6f,
                tempDurationSeconds = currentPhoto?.durationSeconds ?: 1.5f
            )
        }
    }

    fun closeSheet() {
        _uiState.update { it.copy(activeSheet = EditorSheet.NONE) }
    }

    // Animation Sheet Actions
    fun setAnimationCategory(category: AnimationCategory) {
        _uiState.update { it.copy(tempAnimationCategory = category) }
    }

    fun selectTempAnimation(type: AnimationType) {
        _uiState.update { it.copy(tempAnimationType = type) }
    }

    fun applyAnimation(applyToAll: Boolean) {
        val currentPhoto = _uiState.value.selectedPhoto ?: return
        val anim = _uiState.value.tempAnimationType
        viewModelScope.launch {
            photoRepository.updatePhotoAnimation(currentPhoto.id, anim, applyToAll)
            closeSheet()
        }
    }

    // Transition Sheet Actions
    fun selectTempTransition(type: TransitionType) {
        _uiState.update { it.copy(tempTransitionType = type) }
    }

    fun setTempTransitionDuration(duration: Float) {
        _uiState.update { it.copy(tempTransitionDuration = duration) }
    }

    fun applyTransition(applyToAll: Boolean) {
        val currentPhoto = _uiState.value.selectedPhoto ?: return
        val trans = _uiState.value.tempTransitionType
        val dur = _uiState.value.tempTransitionDuration
        viewModelScope.launch {
            photoRepository.updatePhotoTransition(currentPhoto.id, trans, dur, applyToAll)
            closeSheet()
        }
    }

    // Duration Sheet Actions
    fun setTempDuration(duration: Float) {
        _uiState.update { it.copy(tempDurationSeconds = duration) }
    }

    fun setApplyDurationToAll(apply: Boolean) {
        _uiState.update { it.copy(applyDurationToAll = apply) }
    }

    fun applyDuration() {
        val currentPhoto = _uiState.value.selectedPhoto ?: return
        val dur = _uiState.value.tempDurationSeconds
        val applyAll = _uiState.value.applyDurationToAll
        viewModelScope.launch {
            photoRepository.updatePhotoDuration(currentPhoto.id, dur, applyAll)
            closeSheet()
        }
    }

    // Music Actions
    fun selectMusic(track: MusicTrack) {
        viewModelScope.launch {
            musicRepository.selectTrack(track.id)
            closeSheet()
        }
    }
}
