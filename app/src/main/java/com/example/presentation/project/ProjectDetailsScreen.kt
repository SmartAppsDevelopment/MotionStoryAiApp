package com.example.presentation.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.core.theme.MotionBgDark
import com.example.core.theme.MotionBorder
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionRed
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.DeleteProjectDialog
import com.example.core.ui.components.MotionStoryIconButton
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStoryTopBar

@Composable
fun ProjectDetailsScreen(
    projectId: String,
    onBackClick: () -> Unit,
    onContinueEditing: (String) -> Unit,
    onExportClick: (String) -> Unit,
    onPreviewClick: (String) -> Unit,
    viewModel: ProjectDetailsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val project = uiState.project

    Scaffold(
        containerColor = MotionBgDark,
        topBar = {
            MotionStoryTopBar(
                title = project?.title ?: "Project Details",
                onBackClick = onBackClick,
                trailingContent = {
                    MotionStoryIconButton(
                        icon = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        onClick = { viewModel.showRename() },
                        size = 38.dp
                    )
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (project != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Large video/photo preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.15f)
                            .clip(RoundedCornerShape(22.dp))
                            .border(1.dp, MotionBorder, RoundedCornerShape(22.dp))
                            .clickable { onPreviewClick(project.id) }
                    ) {
                        Image(
                            painter = painterResource(id = project.coverResId),
                            contentDescription = project.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Center play button
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Duration badge on bottom right
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.7f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = project.durationFormatted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Title & edited timestamp
                    Text(
                        text = project.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MotionTextPrimary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "Last edited ${project.editedAgo}",
                        fontSize = 13.sp,
                        color = MotionTextSecondary,
                        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                    )

                    // 3 Stats Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            icon = Icons.Default.PhotoLibrary,
                            value = "${project.photoCount}",
                            label = "photos",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.Timer,
                            value = "18",
                            label = "seconds",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.Tv,
                            value = project.resolution,
                            label = "quality",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Continue Editing Primary Button
                    MotionStoryPrimaryButton(
                        text = "Continue Editing",
                        onClick = { onContinueEditing(project.id) },
                        leadingIcon = Icons.Default.Edit,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 Action Buttons Grid (2x2)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProjectActionCard(
                            icon = Icons.Default.Edit,
                            label = "Rename",
                            onClick = { viewModel.showRename() },
                            modifier = Modifier.weight(1f)
                        )
                        ProjectActionCard(
                            icon = Icons.Default.ContentCopy,
                            label = "Duplicate",
                            onClick = { viewModel.duplicate(onBackClick) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProjectActionCard(
                            icon = Icons.Default.FileDownload,
                            label = "Export",
                            onClick = { onExportClick(project.id) },
                            modifier = Modifier.weight(1f)
                        )
                        ProjectActionCard(
                            icon = Icons.Default.Delete,
                            label = "Delete",
                            textColor = MotionRed,
                            onClick = { viewModel.showDelete() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    // Delete Dialog
    if (uiState.showDeleteDialog && project != null) {
        DeleteProjectDialog(
            projectTitle = project.title,
            onDismiss = { viewModel.dismissDelete() },
            onConfirmDelete = { viewModel.confirmDelete(onBackClick) }
        )
    }

    // Rename Dialog
    if (uiState.showRenameDialog && project != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissRename() },
            title = {
                Text(
                    text = "Rename Project",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MotionTextPrimary
                )
            },
            text = {
                OutlinedTextField(
                    value = uiState.renameText,
                    onValueChange = { viewModel.updateRenameText(it) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmRename() }) {
                    Text("Save", color = MotionPurplePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissRename() }) {
                    Text("Cancel", color = MotionTextSecondary)
                }
            },
            containerColor = MotionSurfaceCard,
            shape = RoundedCornerShape(18.dp)
        )
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MotionSurfaceCard)
            .border(1.dp, MotionBorder, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MotionPurplePrimary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MotionTextPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = MotionTextMuted
            )
        }
    }
}

@Composable
private fun ProjectActionCard(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MotionTextPrimary
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MotionSurfaceCard)
            .border(1.dp, MotionBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(17.dp)
            )
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
