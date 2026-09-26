package com.example.data.repository

import com.example.data.model.MusicTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MusicRepository {
    fun getTracks(): Flow<List<MusicTrack>>
    fun getSelectedTrack(): Flow<MusicTrack?>
    suspend fun selectTrack(trackId: String)
    suspend fun updateTrackSettings(
        trackId: String,
        startSec: Float,
        endSec: Float,
        fadeIn: Boolean,
        fadeOut: Boolean,
        volume: Float,
        originalAudioVolume: Float
    )
}

class FakeMusicRepository : MusicRepository {
    private val tracksList = listOf(
        MusicTrack(
            id = "track_1",
            title = "Sunset Drive",
            artist = "Maya Woods",
            durationFormatted = "2:48",
            durationSeconds = 168,
            colorHex = 0xFFF97316,
            isSelected = true
        ),
        MusicTrack(
            id = "track_2",
            title = "Golden Hours",
            artist = "North & June",
            durationFormatted = "3:12",
            durationSeconds = 192,
            colorHex = 0xFFFBBF24,
            isSelected = false
        ),
        MusicTrack(
            id = "track_3",
            title = "Somewhere New",
            artist = "The Coastline",
            durationFormatted = "2:36",
            durationSeconds = 156,
            colorHex = 0xFF2DD4BF,
            isSelected = false
        ),
        MusicTrack(
            id = "track_4",
            title = "Little Moments",
            artist = "Olive Lane",
            durationFormatted = "3:05",
            durationSeconds = 185,
            colorHex = 0xFFF472B6,
            isSelected = false
        ),
        MusicTrack(
            id = "track_5",
            title = "Open Roads",
            artist = "Field Notes",
            durationFormatted = "2:57",
            durationSeconds = 177,
            colorHex = 0xFF4ADE80,
            isSelected = false
        )
    )

    private val _tracks = MutableStateFlow(tracksList)
    private val _selectedTrack = MutableStateFlow<MusicTrack?>(tracksList.first())

    override fun getTracks(): Flow<List<MusicTrack>> = _tracks.asStateFlow()

    override fun getSelectedTrack(): Flow<MusicTrack?> = _selectedTrack.asStateFlow()

    override suspend fun selectTrack(trackId: String) {
        val updated = _tracks.value.map {
            it.copy(isSelected = (it.id == trackId))
        }
        _tracks.value = updated
        _selectedTrack.value = updated.find { it.id == trackId }
    }

    override suspend fun updateTrackSettings(
        trackId: String,
        startSec: Float,
        endSec: Float,
        fadeIn: Boolean,
        fadeOut: Boolean,
        volume: Float,
        originalAudioVolume: Float
    ) {
        val current = _selectedTrack.value ?: return
        val updated = current.copy(
            startTrimSec = startSec,
            endTrimSec = endSec,
            fadeIn = fadeIn,
            fadeOut = fadeOut,
            musicVolume = volume,
            originalAudioVolume = originalAudioVolume
        )
        _selectedTrack.value = updated
        _tracks.value = _tracks.value.map { if (it.id == trackId) updated else it }
    }
}
