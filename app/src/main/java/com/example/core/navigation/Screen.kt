package com.example.core.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object SelectPhotos : Screen("select_photos")
    data object ReviewPhotos : Screen("review_photos")
    data object RearrangePhotos : Screen("rearrange_photos")
    data object ProjectDetails : Screen("project_details/{projectId}") {
        fun createRoute(projectId: String) = "project_details/$projectId"
    }
    data object Editor : Screen("editor?projectId={projectId}") {
        fun createRoute(projectId: String? = null) = if (projectId != null) "editor?projectId=$projectId" else "editor"
    }
    data object PhotoEditor : Screen("photo_editor/{photoId}") {
        fun createRoute(photoId: String) = "photo_editor/$photoId"
    }
    data object TextEditor : Screen("text_editor/{photoId}") {
        fun createRoute(photoId: String) = "text_editor/$photoId"
    }
    data object EditAudio : Screen("edit_audio/{trackId}") {
        fun createRoute(trackId: String) = "edit_audio/$trackId"
    }
    data object VideoPreview : Screen("video_preview?projectId={projectId}") {
        fun createRoute(projectId: String? = null) = if (projectId != null) "video_preview?projectId=$projectId" else "video_preview"
    }
    data object ExportSettings : Screen("export_settings?projectId={projectId}") {
        fun createRoute(projectId: String? = null) = if (projectId != null) "export_settings?projectId=$projectId" else "export_settings"
    }
    data object ExportProgress : Screen("export_progress?projectId={projectId}") {
        fun createRoute(projectId: String? = null) = if (projectId != null) "export_progress?projectId=$projectId" else "export_progress"
    }
    data object ExportSuccess : Screen("export_success?projectId={projectId}") {
        fun createRoute(projectId: String? = null) = if (projectId != null) "export_success?projectId=$projectId" else "export_success"
    }
    data object Settings : Screen("settings")
}
