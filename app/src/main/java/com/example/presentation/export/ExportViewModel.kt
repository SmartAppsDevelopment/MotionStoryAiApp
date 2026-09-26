package com.example.presentation.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ExportSettings
import com.example.data.model.Project
import com.example.data.repository.FakeProjectRepository
import com.example.data.repository.ProjectRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExportUiState(
    val project: Project? = null,
    val settings: ExportSettings = ExportSettings(),
    val progressPercent: Int = 0,
    val progressStatusText: String = "Preparing assets...",
    val isExportFinished: Boolean = false
)

class ExportViewModel(
    private val projectRepository: ProjectRepository = FakeProjectRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportUiState())
    val uiState = _uiState.asStateFlow()

    fun loadProject(projectId: String?) {
        viewModelScope.launch {
            val list = listOfNotNull(
                if (projectId != null) projectRepository.getProjectById(projectId) else null
            )
            val current = list.firstOrNull() ?: projectRepository.getProjectById("project_1")
            _uiState.update { it.copy(project = current) }
        }
    }

    fun selectAspectRatio(ratio: String) {
        _uiState.update { it.copy(settings = it.settings.copy(aspectRatio = ratio)) }
    }

    fun selectResolution(res: String) {
        val size = when (res) {
            "720p" -> 24
            "1080p" -> 42
            "4K" -> 118
            else -> 42
        }
        _uiState.update { it.copy(settings = it.settings.copy(resolution = res, estimatedSizeMb = size)) }
    }

    fun selectFps(fps: String) {
        _uiState.update { it.copy(settings = it.settings.copy(fps = fps)) }
    }

    fun selectQuality(quality: String) {
        _uiState.update { it.copy(settings = it.settings.copy(quality = quality)) }
    }

    fun startExportSimulation(onFinished: () -> Unit) {
        _uiState.update { it.copy(progressPercent = 0, isExportFinished = false) }
        viewModelScope.launch {
            val milestones = listOf(
                15 to "Loading photos and audio...",
                38 to "Applying motion and animations...",
                64 to "Rendering video transitions...",
                82 to "Applying animations and transitions...",
                95 to "Finalizing video encode...",
                100 to "Done!"
            )
            for ((percent, text) in milestones) {
                delay(600)
                _uiState.update { it.copy(progressPercent = percent, progressStatusText = text) }
            }
            delay(400)
            _uiState.update { it.copy(isExportFinished = true) }
            onFinished()
        }
    }
}
