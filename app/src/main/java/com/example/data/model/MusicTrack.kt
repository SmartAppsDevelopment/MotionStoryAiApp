package com.example.data.model

data class MusicTrack(
    val id: String,
    val title: String,
    val artist: String,
    val durationFormatted: String,
    val durationSeconds: Int = 168,
    val category: String = "Travel & memories",
    val colorHex: Long = 0xFFF97316,
    val isSelected: Boolean = false,
    val startTrimSec: Float = 12.4f,
    val endTrimSec: Float = 30.4f,
    val fadeIn: Boolean = true,
    val fadeOut: Boolean = true,
    val musicVolume: Float = 72f,
    val originalAudioVolume: Float = 0f
)
