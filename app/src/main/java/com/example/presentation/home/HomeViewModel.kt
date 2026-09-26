package com.example.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Project
import com.example.data.repository.FakeProjectRepository
import com.example.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val projectRepository: ProjectRepository = FakeProjectRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            projectRepository.getProjects().collect { projects ->
                _uiState.update { it.copy(projects = projects) }
            }
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun onProjectMenuClick(project: Project) {
        _uiState.update { it.copy(selectedProjectForMenu = project) }
    }

    fun dismissMenu() {
        _uiState.update { it.copy(selectedProjectForMenu = null) }
    }

    fun requestDeleteProject() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun confirmDelete() {
        val project = _uiState.value.selectedProjectForMenu
        if (project != null) {
            viewModelScope.launch {
                projectRepository.deleteProject(project.id)
                _uiState.update { it.copy(showDeleteDialog = false, selectedProjectForMenu = null) }
            }
        }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun duplicateCurrentProject() {
        val project = _uiState.value.selectedProjectForMenu
        if (project != null) {
            viewModelScope.launch {
                projectRepository.duplicateProject(project.id)
                _uiState.update { it.copy(selectedProjectForMenu = null) }
            }
        }
    }

    fun showRename() {
        val currentTitle = _uiState.value.selectedProjectForMenu?.title ?: ""
        _uiState.update { it.copy(showRenameDialog = true, renameText = currentTitle) }
    }

    fun updateRenameText(text: String) {
        _uiState.update { it.copy(renameText = text) }
    }

    fun confirmRename() {
        val project = _uiState.value.selectedProjectForMenu
        val newTitle = _uiState.value.renameText
        if (project != null && newTitle.isNotBlank()) {
            viewModelScope.launch {
                projectRepository.renameProject(project.id, newTitle)
                _uiState.update { it.copy(showRenameDialog = false, selectedProjectForMenu = null) }
            }
        }
    }

    fun dismissRenameDialog() {
        _uiState.update { it.copy(showRenameDialog = false) }
    }
}
