package com.example.core.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.presentation.audio.EditAudioScreen
import com.example.presentation.editor.EditorScreen
import com.example.presentation.export.ExportProgressScreen
import com.example.presentation.export.ExportSettingsScreen
import com.example.presentation.export.ExportSuccessScreen
import com.example.presentation.gallery.RearrangePhotosScreen
import com.example.presentation.gallery.ReviewPhotosScreen
import com.example.presentation.gallery.SelectPhotosScreen
import com.example.presentation.home.HomeScreen
import com.example.presentation.onboarding.OnboardingScreen
import com.example.presentation.photoeditor.PhotoEditorScreen
import com.example.presentation.preview.PreviewScreen
import com.example.presentation.project.ProjectDetailsScreen
import com.example.presentation.settings.SettingsScreen
import com.example.presentation.texteditor.TextEditorScreen

@Composable
fun MotionStoryNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onCreateNewVideo = {
                    navController.navigate(Screen.SelectPhotos.route)
                },
                onProjectClick = { projectId ->
                    navController.navigate(Screen.ProjectDetails.createRoute(projectId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onExportProject = { projectId ->
                    navController.navigate(Screen.ExportSettings.createRoute(projectId))
                }
            )
        }

        composable(Screen.SelectPhotos.route) {
            SelectPhotosScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = {
                    navController.navigate(Screen.ReviewPhotos.route)
                }
            )
        }

        composable(Screen.ReviewPhotos.route) {
            ReviewPhotosScreen(
                onBackClick = { navController.popBackStack() },
                onAddMorePhotos = { navController.popBackStack() },
                onRearrangeClick = {
                    navController.navigate(Screen.RearrangePhotos.route)
                },
                onContinueEditing = {
                    navController.navigate(Screen.Editor.createRoute("project_1"))
                },
                onCreateVideo = {
                    navController.navigate(Screen.Editor.createRoute("project_1"))
                }
            )
        }

        composable(Screen.RearrangePhotos.route) {
            RearrangePhotosScreen(
                onBackClick = { navController.popBackStack() },
                onDoneClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ProjectDetails.route,
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: "project_1"
            ProjectDetailsScreen(
                projectId = projectId,
                onBackClick = { navController.popBackStack() },
                onContinueEditing = { id ->
                    navController.navigate(Screen.Editor.createRoute(id))
                },
                onExportClick = { id ->
                    navController.navigate(Screen.ExportSettings.createRoute(id))
                },
                onPreviewClick = { id ->
                    navController.navigate(Screen.VideoPreview.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.Editor.route,
            arguments = listOf(navArgument("projectId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            EditorScreen(
                projectId = projectId,
                onBackClick = { navController.popBackStack() },
                onPreviewClick = {
                    navController.navigate(Screen.VideoPreview.createRoute(projectId))
                },
                onNavigateToPhotoEditor = { photoId ->
                    navController.navigate(Screen.PhotoEditor.createRoute(photoId))
                },
                onNavigateToTextEditor = { photoId ->
                    navController.navigate(Screen.TextEditor.createRoute(photoId))
                },
                onNavigateToAudioEditor = { trackId ->
                    navController.navigate(Screen.EditAudio.createRoute(trackId))
                },
                onNavigateToPhotosList = {
                    navController.navigate(Screen.ReviewPhotos.route)
                }
            )
        }

        composable(
            route = Screen.PhotoEditor.route,
            arguments = listOf(navArgument("photoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId") ?: "photo_1"
            PhotoEditorScreen(
                photoId = photoId,
                onBackClick = { navController.popBackStack() },
                onDoneClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TextEditor.route,
            arguments = listOf(navArgument("photoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getString("photoId") ?: "photo_1"
            TextEditorScreen(
                photoId = photoId,
                onBackClick = { navController.popBackStack() },
                onApplyClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditAudio.route,
            arguments = listOf(navArgument("trackId") { type = NavType.StringType })
        ) { backStackEntry ->
            val trackId = backStackEntry.arguments?.getString("trackId") ?: "track_1"
            EditAudioScreen(
                trackId = trackId,
                onBackClick = { navController.popBackStack() },
                onApplyClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.VideoPreview.route,
            arguments = listOf(navArgument("projectId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            PreviewScreen(
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(Screen.Editor.createRoute(projectId))
                },
                onMusicClick = {
                    navController.navigate(Screen.EditAudio.createRoute("track_1"))
                },
                onShareClick = {
                    navController.navigate(Screen.ExportSettings.createRoute(projectId))
                }
            )
        }

        composable(
            route = Screen.ExportSettings.route,
            arguments = listOf(navArgument("projectId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            ExportSettingsScreen(
                projectId = projectId,
                onBackClick = { navController.popBackStack() },
                onStartExport = {
                    navController.navigate(Screen.ExportProgress.createRoute(projectId))
                }
            )
        }

        composable(
            route = Screen.ExportProgress.route,
            arguments = listOf(navArgument("projectId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            ExportProgressScreen(
                projectId = projectId,
                onExportComplete = {
                    navController.navigate(Screen.ExportSuccess.createRoute(projectId)) {
                        popUpTo(Screen.ExportProgress.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.ExportSuccess.route,
            arguments = listOf(navArgument("projectId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            ExportSuccessScreen(
                projectId = projectId,
                onSaveToGallery = {
                    Toast.makeText(context, "Saved video to gallery!", Toast.LENGTH_SHORT).show()
                },
                onShareVideo = {
                    Toast.makeText(context, "Opening share menu...", Toast.LENGTH_SHORT).show()
                },
                onEditAgain = {
                    navController.navigate(Screen.Editor.createRoute(projectId)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onCreateNewVideo = {
                    navController.navigate(Screen.SelectPhotos.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
