package com.example.presentation.home

import com.example.data.model.Project

data class HomeUiState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTab: Int = 0,
    val selectedProjectForMenu: Project? = null,
    val showDeleteDialog: Boolean = false,
    val showRenameDialog: Boolean = false,
    val renameText: String = ""
)
