package com.example.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.core.theme.MotionBgDark
import com.example.core.theme.MotionBorder
import com.example.core.theme.MotionPurpleDark
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionRed
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceDark
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.DeleteProjectDialog
import com.example.core.ui.components.MotionStoryIconButton
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.ProjectCard
import com.example.data.model.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCreateNewVideo: () -> Unit,
    onProjectClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onExportProject: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = MotionBgDark,
        bottomBar = {
            HomeBottomNavigationBar(
                selectedTab = uiState.selectedTab,
                onTabSelect = { index ->
                    if (index == 1) {
                        onCreateNewVideo()
                    } else if (index == 3) {
                        onSettingsClick()
                    } else {
                        viewModel.selectTab(index)
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Home Top Bar
            HomeTopBar(onSettingsClick = onSettingsClick)

            if (uiState.projects.isEmpty()) {
                // Empty state matching "Home — empty state.png"
                HomeEmptyState(onCreateNewVideo = onCreateNewVideo)
            } else {
                // Projects Grid matching "Home — recent projects.png"
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Hero Card as full width item
                    item(span = { GridItemSpan(2) }) {
                        HomeHeroBanner(onCreateNewVideo = onCreateNewVideo)
                    }

                    // Section Header
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent Projects",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MotionTextPrimary
                            )
                            Text(
                                text = "See all",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MotionPurplePrimary,
                                modifier = Modifier.clickable { /* See all projects */ }
                            )
                        }
                    }

                    // Project Cards (2 columns)
                    items(uiState.projects, key = { it.id }) { project ->
                        ProjectCard(
                            project = project,
                            onClick = { onProjectClick(project.id) },
                            onMenuClick = { viewModel.onProjectMenuClick(project) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    // Project Options Modal Bottom Sheet
    if (uiState.selectedProjectForMenu != null && !uiState.showDeleteDialog && !uiState.showRenameDialog) {
        val project = uiState.selectedProjectForMenu!!
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissMenu() },
            sheetState = bottomSheetState,
            containerColor = MotionSurfaceDark,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .size(width = 36.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF383A4C))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = project.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MotionTextPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Menu items
                ProjectMenuRow(
                    icon = Icons.Default.Edit,
                    label = "Rename",
                    onClick = { viewModel.showRename() }
                )
                ProjectMenuRow(
                    icon = Icons.Default.ContentCopy,
                    label = "Duplicate",
                    onClick = { viewModel.duplicateCurrentProject() }
                )
                ProjectMenuRow(
                    icon = Icons.Default.FileDownload,
                    label = "Export Video",
                    onClick = {
                        viewModel.dismissMenu()
                        onExportProject(project.id)
                    }
                )
                ProjectMenuRow(
                    icon = Icons.Default.Delete,
                    label = "Delete",
                    textColor = MotionRed,
                    onClick = { viewModel.requestDeleteProject() }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Delete Dialog
    if (uiState.showDeleteDialog && uiState.selectedProjectForMenu != null) {
        DeleteProjectDialog(
            projectTitle = uiState.selectedProjectForMenu!!.title,
            onDismiss = { viewModel.dismissDeleteDialog() },
            onConfirmDelete = { viewModel.confirmDelete() }
        )
    }

    // Rename Dialog
    if (uiState.showRenameDialog && uiState.selectedProjectForMenu != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissRenameDialog() },
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
                TextButton(onClick = { viewModel.dismissRenameDialog() }) {
                    Text("Cancel", color = MotionTextSecondary)
                }
            },
            containerColor = MotionSurfaceCard,
            shape = RoundedCornerShape(18.dp)
        )
    }
}

@Composable
private fun HomeTopBar(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Icon squircle
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = "MotionStory",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MotionTextPrimary
        )

        MotionStoryIconButton(
            icon = Icons.Outlined.Settings,
            contentDescription = "Settings",
            onClick = onSettingsClick,
            tint = MotionTextPrimary,
            size = 40.dp
        )
    }
}

@Composable
private fun HomeHeroBanner(onCreateNewVideo: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF4C2889), Color(0xFF281458))
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "PHOTO STORY MAKER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            Text(
                text = "Turn your photos into\nbeautiful videos",
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 10.dp, bottom = 18.dp)
            )

            // Pill Create button with white background
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.White)
                    .clickable(onClick = onCreateNewVideo)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MotionPurpleDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Create New Video",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MotionPurpleDark
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeEmptyState(onCreateNewVideo: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_home_polaroids),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Purple circle play button in center
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MotionPurplePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Create your first photo\nvideo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MotionTextPrimary,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )

        Text(
            text = "Choose favorite moments, add motion and\nmusic, then share a story worth replaying.",
            fontSize = 14.sp,
            color = MotionTextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 26.dp)
        )

        MotionStoryPrimaryButton(
            text = "+ Create New Video",
            onClick = onCreateNewVideo,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "It only takes a few minutes",
            fontSize = 12.sp,
            color = MotionTextMuted,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
private fun ProjectMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    textColor: Color = MotionTextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(start = 14.dp)
        )
    }
}

@Composable
fun HomeBottomNavigationBar(
    selectedTab: Int,
    onTabSelect: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MotionSurfaceDark,
        modifier = Modifier
            .navigationBarsPadding()
            .height(60.dp)
    ) {
        val items = listOf(
            Triple(0, "Home", Icons.Default.Home),
            Triple(1, "Create", Icons.Default.AddBox),
            Triple(2, "Projects", Icons.Default.Folder),
            Triple(3, "Settings", Icons.Outlined.Settings)
        )

        items.forEach { (index, label, icon) ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelect(index) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) MotionPurplePrimary else MotionTextMuted
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) MotionPurplePrimary else MotionTextMuted
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
