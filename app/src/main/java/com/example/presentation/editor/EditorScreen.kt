package com.example.presentation.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceDark
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTeal
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.EditorTopBar
import com.example.core.ui.components.MotionStoryIconButton
import com.example.core.ui.components.TimelinePhotoCard
import com.example.presentation.editor.sheets.AnimationBottomSheet
import com.example.presentation.editor.sheets.DurationBottomSheet
import com.example.presentation.editor.sheets.MusicBottomSheet
import com.example.presentation.editor.sheets.TransitionBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    projectId: String?,
    onBackClick: () -> Unit,
    onPreviewClick: () -> Unit,
    onNavigateToPhotoEditor: (String) -> Unit,
    onNavigateToTextEditor: (String) -> Unit,
    onNavigateToAudioEditor: (String) -> Unit,
    onNavigateToPhotosList: () -> Unit,
    viewModel: EditorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val currentPhoto = uiState.selectedPhoto

    val animationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val transitionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val durationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val musicSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = MotionBgDark,
        topBar = {
            EditorTopBar(
                title = uiState.projectTitle,
                onBackClick = onBackClick,
                onPreviewClick = onPreviewClick,
                statusText = "Saved"
            )
        },
        bottomBar = {
            EditorBottomToolbar(
                onPhotosClick = onNavigateToPhotosList,
                onEditClick = {
                    if (currentPhoto != null) {
                        onNavigateToPhotoEditor(currentPhoto.id)
                    }
                },
                onAnimationClick = { viewModel.openSheet(EditorSheet.ANIMATION) },
                onTransitionClick = { viewModel.openSheet(EditorSheet.TRANSITION) },
                onDurationClick = { viewModel.openSheet(EditorSheet.DURATION) },
                onTextClick = {
                    if (currentPhoto != null) {
                        onNavigateToTextEditor(currentPhoto.id)
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Main Photo / Video Preview Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, MotionBorder, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (currentPhoto != null) {
                    Image(
                        painter = painterResource(id = currentPhoto.drawableResId),
                        contentDescription = currentPhoto.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dashed guide border overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )

                    // Floating Motion/Animation badge on bottom left
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MotionPurplePrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "${currentPhoto.animation.title} • Motion",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Media Controls Row: Photo 1 of 12, Prev, Play/Pause, Next, Fullscreen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Photo ${uiState.selectedPhotoIndex + 1} of ${uiState.photos.size}",
                    fontSize = 12.sp,
                    color = MotionTextSecondary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { viewModel.previousPhoto() }
                    )

                    // Play/Pause button
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MotionPurplePrimary)
                            .clickable { viewModel.togglePlayPause() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { viewModel.nextPhoto() }
                    )
                }

                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Fullscreen",
                    tint = MotionTextSecondary,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable(onClick = onPreviewClick)
                )
            }

            // Time & Music Status Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "00:06.0 / 00:18.0",
                    fontSize = 12.sp,
                    color = MotionTextSecondary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { viewModel.openSheet(EditorSheet.MUSIC) }
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = MotionTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (uiState.selectedMusic != null) "${uiState.selectedMusic!!.title} added" else "Add music",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MotionTeal
                    )
                }
            }

            // Timeline Row with photos & [+] button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MotionSurfaceCard)
                    .border(1.dp, MotionBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 10.dp, horizontal = 8.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    itemsIndexed(uiState.photos, key = { _, item -> item.id }) { index, photo ->
                        TimelinePhotoCard(
                            photo = photo,
                            isSelected = index == uiState.selectedPhotoIndex,
                            onClick = { viewModel.selectPhoto(index) }
                        )
                    }

                    // Add Photo (+) Button
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable(onClick = onNavigateToPhotosList)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MotionSurfaceVariant)
                                    .border(1.dp, MotionBorder, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add photo",
                                    tint = MotionTextSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    // Modal Bottom Sheets
    when (uiState.activeSheet) {
        EditorSheet.ANIMATION -> {
            AnimationBottomSheet(
                sheetState = animationSheetState,
                selectedCategory = uiState.tempAnimationCategory,
                selectedAnimation = uiState.tempAnimationType,
                onCategorySelect = { viewModel.setAnimationCategory(it) },
                onAnimationSelect = { viewModel.selectTempAnimation(it) },
                onApplyToAll = { viewModel.applyAnimation(applyToAll = true) },
                onApply = { viewModel.applyAnimation(applyToAll = false) },
                onDismiss = { viewModel.closeSheet() }
            )
        }
        EditorSheet.TRANSITION -> {
            TransitionBottomSheet(
                sheetState = transitionSheetState,
                selectedTransition = uiState.tempTransitionType,
                transitionDuration = uiState.tempTransitionDuration,
                onTransitionSelect = { viewModel.selectTempTransition(it) },
                onDurationChange = { viewModel.setTempTransitionDuration(it) },
                onApplyToCurrent = { viewModel.applyTransition(applyToAll = false) },
                onApplyToAll = { viewModel.applyTransition(applyToAll = true) },
                onDismiss = { viewModel.closeSheet() }
            )
        }
        EditorSheet.DURATION -> {
            DurationBottomSheet(
                sheetState = durationSheetState,
                currentDuration = uiState.tempDurationSeconds,
                applyToAll = uiState.applyDurationToAll,
                totalPhotosCount = uiState.photos.size,
                onDurationChange = { viewModel.setTempDuration(it) },
                onApplyToAllChange = { viewModel.setApplyDurationToAll(it) },
                onDone = { viewModel.applyDuration() },
                onDismiss = { viewModel.closeSheet() }
            )
        }
        EditorSheet.MUSIC -> {
            MusicBottomSheet(
                sheetState = musicSheetState,
                selectedMusic = uiState.selectedMusic,
                onSelectMusic = { viewModel.selectMusic(it) },
                onOpenAudioEditor = {
                    viewModel.closeSheet()
                    onNavigateToAudioEditor(it)
                },
                onDismiss = { viewModel.closeSheet() }
            )
        }
        EditorSheet.NONE -> { /* No sheet */ }
    }
}

@Composable
private fun EditorBottomToolbar(
    onPhotosClick: () -> Unit,
    onEditClick: () -> Unit,
    onAnimationClick: () -> Unit,
    onTransitionClick: () -> Unit,
    onDurationClick: () -> Unit,
    onTextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .height(68.dp)
            .background(MotionSurfaceDark)
            .border(1.dp, MotionBorder)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToolbarItem(
            icon = Icons.Default.PhotoLibrary,
            label = "Photos",
            onClick = onPhotosClick,
            isActive = true
        )
        ToolbarItem(
            icon = Icons.Default.Tune,
            label = "Edit",
            onClick = onEditClick
        )
        ToolbarItem(
            icon = Icons.Default.AutoAwesome,
            label = "Animation",
            onClick = onAnimationClick
        )
        ToolbarItem(
            icon = Icons.Default.Link,
            label = "Transition",
            onClick = onTransitionClick
        )
        ToolbarItem(
            icon = Icons.Default.Timer,
            label = "Duration",
            onClick = onDurationClick
        )
        ToolbarItem(
            icon = Icons.Default.TextFields,
            label = "Text",
            onClick = onTextClick
        )
    }
}

@Composable
private fun ToolbarItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isActive: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isActive) Color(0xFF2E2452) else MotionSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) MotionPurplePrimary else MotionTextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isActive) MotionPurplePrimary else MotionTextMuted,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
