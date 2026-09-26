package com.example.presentation.gallery

import com.example.data.model.PhotoItem

data class SelectPhotosUiState(
    val availablePhotos: List<PhotoItem> = emptyList(),
    val selectedPhotos: List<PhotoItem> = emptyList(),
    val selectedTab: Int = 0 // 0: Photos, 1: Albums, 2: Recent, 3: Favorites
)
