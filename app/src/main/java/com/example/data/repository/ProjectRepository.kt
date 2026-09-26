package com.example.data.repository

import com.example.R
import com.example.data.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ProjectRepository {
    fun getProjects(): Flow<List<Project>>
    suspend fun getProjectById(projectId: String): Project?
    suspend fun createProject(title: String): Project
    suspend fun deleteProject(projectId: String)
    suspend fun duplicateProject(projectId: String): Project?
    suspend fun renameProject(projectId: String, newTitle: String)
}

class FakeProjectRepository : ProjectRepository {
    private val initialProjects = listOf(
        Project(
            id = "project_1",
            title = "Summer Trip",
            coverResId = R.drawable.sample_lake_como,
            photoCount = 12,
            durationFormatted = "00:18",
            editedAgo = "Edited 12 min ago",
            resolution = "1080p",
            quality = "High",
            fps = "30 FPS",
            aspectRatio = "9:16"
        ),
        Project(
            id = "project_2",
            title = "Birthday Memories",
            coverResId = R.drawable.sample_birthday,
            photoCount = 18,
            durationFormatted = "00:26",
            editedAgo = "Edited yesterday",
            resolution = "1080p",
            quality = "High",
            fps = "30 FPS",
            aspectRatio = "9:16"
        ),
        Project(
            id = "project_3",
            title = "Family Moments",
            coverResId = R.drawable.sample_beach,
            photoCount = 9,
            durationFormatted = "00:14",
            editedAgo = "Edited Sep 20",
            resolution = "1080p",
            quality = "High",
            fps = "30 FPS",
            aspectRatio = "9:16"
        ),
        Project(
            id = "project_4",
            title = "My Vacation",
            coverResId = R.drawable.sample_cove,
            photoCount = 15,
            durationFormatted = "00:22",
            editedAgo = "Edited Sep 14",
            resolution = "1080p",
            quality = "High",
            fps = "30 FPS",
            aspectRatio = "9:16"
        )
    )

    private val _projects = MutableStateFlow(initialProjects)

    override fun getProjects(): Flow<List<Project>> = _projects.asStateFlow()

    override suspend fun getProjectById(projectId: String): Project? {
        return _projects.value.find { it.id == projectId }
    }

    override suspend fun createProject(title: String): Project {
        val newProj = Project(
            id = "project_${System.currentTimeMillis()}",
            title = title.ifBlank { "Untitled Story" },
            coverResId = R.drawable.sample_lake_como,
            photoCount = 4,
            durationFormatted = "00:12",
            editedAgo = "Just now"
        )
        _projects.value = listOf(newProj) + _projects.value
        return newProj
    }

    override suspend fun deleteProject(projectId: String) {
        _projects.value = _projects.value.filterNot { it.id == projectId }
    }

    override suspend fun duplicateProject(projectId: String): Project? {
        val original = _projects.value.find { it.id == projectId } ?: return null
        val copy = original.copy(
            id = "project_${System.currentTimeMillis()}",
            title = "${original.title} (Copy)",
            editedAgo = "Just now"
        )
        _projects.value = listOf(copy) + _projects.value
        return copy
    }

    override suspend fun renameProject(projectId: String, newTitle: String) {
        _projects.value = _projects.value.map {
            if (it.id == projectId) it.copy(title = newTitle, editedAgo = "Just now") else it
        }
    }
}
