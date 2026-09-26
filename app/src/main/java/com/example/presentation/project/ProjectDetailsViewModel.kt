package com.example.presentation.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Project
import com.example.data.repository.FakeProjectRepository
import com.example.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProjectDetailsUiState(
    val project: Project? = null,
    val showDeleteDialog: Boolean = false,
    val showRenameDialog: Boolean = false,
    val renameText: String = ""
)

class ProjectDetailsViewModel(
    private val projectRepository: ProjectRepository = FakeProjectRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun loadProject(projectId: String) {
        viewModelScope.launch {
            val proj = projectRepository.getProjectById(projectId)
            _uiState.update { it.copy(project = proj) }
        }
    }

    fun showDelete() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDelete() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun confirmDelete(onDeleted: () -> Unit) {
        val proj = _uiState.value.project ?: return
        viewModelScope.launch {
            projectRepository.deleteProject(proj.id)
            _uiState.update { it.copy(showDeleteDialog = false) }
            onDeleted()
        }
    }

    fun duplicate(onDuplicated: () -> Unit) {
        val proj = _uiState.value.project ?: return
        viewModelScope.launch {
            projectRepository.duplicateProject(proj.id)
            onDuplicated()
        }
    }

    fun showRename() {
        _uiState.update {
            it.copy(
                showRenameDialog = true,
                renameText = it.project?.title ?: ""
            )
        }
    }

    fun updateRenameText(text: String) {
        _uiState.update { it.copy(renameText = text) }
    }

    fun confirmRename() {
        val proj = _uiState.value.project ?: return
        val newTitle = _uiState.value.renameText
        if (newTitle.isNotBlank()) {
            viewModelScope.launch {
                projectRepository.renameProject(proj.id, newTitle)
                _uiState.update {
                    it.copy(
                        showRenameDialog = false,
                        project = it.project?.copy(title = newTitle)
                    )
                }
            }
        }
    }

    fun dismissRename() {
        _uiState.update { it.copy(showRenameDialog = false) }
    }
}
