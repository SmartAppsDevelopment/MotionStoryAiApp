package com.example.data.repository

import com.example.R
import com.example.data.model.AnimationType
import com.example.data.model.PhotoItem
import com.example.data.model.TransitionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface PhotoRepository {
    fun getAvailablePhotos(): Flow<List<PhotoItem>>
    fun getSelectedPhotos(): Flow<List<PhotoItem>>
    suspend fun togglePhotoSelection(photoId: String)
    suspend fun selectPhotos(photoIds: List<String>)
    suspend fun removeSelectedPhoto(photoId: String)
    suspend fun reorderSelectedPhotos(photos: List<PhotoItem>)
    suspend fun updatePhotoDuration(photoId: String, duration: Float, applyToAll: Boolean)
    suspend fun updatePhotoAnimation(photoId: String, animation: AnimationType, applyToAll: Boolean)
    suspend fun updatePhotoTransition(photoId: String, transition: TransitionType, duration: Float, applyToAll: Boolean)
    suspend fun updatePhoto(photo: PhotoItem)
}

class FakePhotoRepository : PhotoRepository {
    private val allPhotosList = listOf(
        PhotoItem(
            id = "photo_1",
            drawableResId = R.drawable.sample_lake_como,
            title = "Photo 1",
            order = 1,
            durationSeconds = 1.5f,
            animation = AnimationType.KEN_BURNS,
            transition = TransitionType.FADE
        ),
        PhotoItem(
            id = "photo_2",
            drawableResId = R.drawable.sample_cove,
            title = "Photo 2",
            order = 2,
            durationSeconds = 2.0f,
            animation = AnimationType.ZOOM_IN,
            transition = TransitionType.DISSOLVE
        ),
        PhotoItem(
            id = "photo_3",
            drawableResId = R.drawable.sample_beach,
            title = "Photo 3",
            order = 3,
            durationSeconds = 1.5f,
            animation = AnimationType.PAN_LEFT,
            transition = TransitionType.SLIDE
        ),
        PhotoItem(
            id = "photo_4",
            drawableResId = R.drawable.sample_cliff_town,
            title = "Photo 4",
            order = 4,
            durationSeconds = 1.5f,
            animation = AnimationType.SLOW_ZOOM,
            transition = TransitionType.FADE
        ),
        PhotoItem(
            id = "photo_5",
            drawableResId = R.drawable.sample_forest,
            title = "Photo 5",
            order = 5,
            durationSeconds = 1.5f,
            animation = AnimationType.KEN_BURNS,
            transition = TransitionType.ZOOM
        ),
        PhotoItem(
            id = "photo_6",
            drawableResId = R.drawable.sample_birthday,
            title = "Photo 6",
            order = 6,
            durationSeconds = 1.5f,
            animation = AnimationType.BOUNCE,
            transition = TransitionType.FADE
        ),
        PhotoItem(
            id = "photo_7",
            drawableResId = R.drawable.sample_lake_como,
            title = "Photo 7",
            order = 7,
            durationSeconds = 1.5f,
            animation = AnimationType.FADE,
            transition = TransitionType.FADE
        ),
        PhotoItem(
            id = "photo_8",
            drawableResId = R.drawable.sample_cove,
            title = "Photo 8",
            order = 8,
            durationSeconds = 2.0f,
            animation = AnimationType.SLIDE_RIGHT,
            transition = TransitionType.WIPE
        ),
        PhotoItem(
            id = "photo_9",
            drawableResId = R.drawable.sample_beach,
            title = "Photo 9",
            order = 9,
            durationSeconds = 1.5f,
            animation = AnimationType.PAN_RIGHT,
            transition = TransitionType.FADE
        )
    )

    private val _availablePhotos = MutableStateFlow(allPhotosList)
    private val _selectedPhotos = MutableStateFlow(allPhotosList.take(4))

    override fun getAvailablePhotos(): Flow<List<PhotoItem>> = _availablePhotos.asStateFlow()

    override fun getSelectedPhotos(): Flow<List<PhotoItem>> = _selectedPhotos.asStateFlow()

    override suspend fun togglePhotoSelection(photoId: String) {
        val current = _selectedPhotos.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.id == photoId }
        if (existingIndex >= 0) {
            current.removeAt(existingIndex)
        } else {
            val item = _availablePhotos.value.find { it.id == photoId }
            if (item != null) {
                current.add(item.copy(order = current.size + 1))
            }
        }
        _selectedPhotos.value = current
    }

    override suspend fun selectPhotos(photoIds: List<String>) {
        val selected = _availablePhotos.value
            .filter { it.id in photoIds }
            .mapIndexed { index, photoItem -> photoItem.copy(order = index + 1) }
        _selectedPhotos.value = selected
    }

    override suspend fun removeSelectedPhoto(photoId: String) {
        val updated = _selectedPhotos.value
            .filterNot { it.id == photoId }
            .mapIndexed { index, photoItem -> photoItem.copy(order = index + 1) }
        _selectedPhotos.value = updated
    }

    override suspend fun reorderSelectedPhotos(photos: List<PhotoItem>) {
        val updated = photos.mapIndexed { index, photoItem -> photoItem.copy(order = index + 1) }
        _selectedPhotos.value = updated
    }

    override suspend fun updatePhotoDuration(photoId: String, duration: Float, applyToAll: Boolean) {
        if (applyToAll) {
            _selectedPhotos.value = _selectedPhotos.value.map { it.copy(durationSeconds = duration) }
        } else {
            _selectedPhotos.value = _selectedPhotos.value.map {
                if (it.id == photoId) it.copy(durationSeconds = duration) else it
            }
        }
    }

    override suspend fun updatePhotoAnimation(photoId: String, animation: AnimationType, applyToAll: Boolean) {
        if (applyToAll) {
            _selectedPhotos.value = _selectedPhotos.value.map { it.copy(animation = animation) }
        } else {
            _selectedPhotos.value = _selectedPhotos.value.map {
                if (it.id == photoId) it.copy(animation = animation) else it
            }
        }
    }

    override suspend fun updatePhotoTransition(
        photoId: String,
        transition: TransitionType,
        duration: Float,
        applyToAll: Boolean
    ) {
        if (applyToAll) {
            _selectedPhotos.value = _selectedPhotos.value.map {
                it.copy(transition = transition, transitionDuration = duration)
            }
        } else {
            _selectedPhotos.value = _selectedPhotos.value.map {
                if (it.id == photoId) it.copy(transition = transition, transitionDuration = duration) else it
            }
        }
    }

    override suspend fun updatePhoto(photo: PhotoItem) {
        _selectedPhotos.value = _selectedPhotos.value.map {
            if (it.id == photo.id) photo else it
        }
    }
}
