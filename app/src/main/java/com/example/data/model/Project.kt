package com.example.data.model

data class Project(
    val id: String,
    val title: String,
    val coverResId: Int,
    val photoCount: Int,
    val durationFormatted: String,
    val editedAgo: String,
    val resolution: String = "1080p",
    val quality: String = "High",
    val fps: String = "30 FPS",
    val aspectRatio: String = "9:16",
    val photos: List<PhotoItem> = emptyList(),
    val music: MusicTrack? = null
)

data class ExportSettings(
    val aspectRatio: String = "9:16",
    val resolution: String = "1080p",
    val fps: String = "30 FPS",
    val quality: String = "High",
    val estimatedSizeMb: Int = 42
)
