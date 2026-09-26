package com.example.presentation.export

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTeal
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStoryTopBar

@Composable
fun ExportSettingsScreen(
    projectId: String?,
    onBackClick: () -> Unit,
    onStartExport: () -> Unit,
    viewModel: ExportViewModel = viewModel(),
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
                title = "Export Video",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                MotionStoryPrimaryButton(
                    text = "Export Video",
                    leadingIcon = Icons.Default.AutoAwesome,
                    onClick = onStartExport,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Project Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MotionSurfaceCard)
                    .border(1.dp, MotionBorder, RoundedCornerShape(18.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = project?.coverResId ?: R.drawable.sample_lake_como),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 68.dp, height = 80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Column(modifier = Modifier.padding(start = 14.dp)) {
                        Text(
                            text = project?.title ?: "Summer Trip",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MotionTextPrimary
                        )
                        Text(
                            text = "${project?.photoCount ?: 12} photos • ${project?.durationFormatted ?: "00:18"} • Music",
                            fontSize = 12.sp,
                            color = MotionTextSecondary,
                            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MotionTeal.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Ready to export",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MotionTeal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Option 1: Aspect ratio
            ExportOptionGroup(
                title = "Aspect ratio",
                options = listOf("9:16", "16:9", "1:1", "4:5"),
                selectedOption = uiState.settings.aspectRatio,
                onSelect = { viewModel.selectAspectRatio(it) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Option 2: Resolution
            ExportOptionGroup(
                title = "Resolution",
                options = listOf("720p", "1080p", "4K"),
                selectedOption = uiState.settings.resolution,
                onSelect = { viewModel.selectResolution(it) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Option 3: Frame rate
            ExportOptionGroup(
                title = "Frame rate",
                options = listOf("24 FPS", "30 FPS", "60 FPS"),
                selectedOption = uiState.settings.fps,
                onSelect = { viewModel.selectFps(it) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Option 4: Quality
            ExportOptionGroup(
                title = "Quality",
                options = listOf("Standard", "High", "Maximum"),
                selectedOption = uiState.settings.quality,
                onSelect = { viewModel.selectQuality(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estimated file size card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MotionSurfaceCard)
                    .border(1.dp, MotionBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Estimated file size",
                        fontSize = 13.sp,
                        color = MotionTextSecondary
                    )
                    Text(
                        text = "≈ ${uiState.settings.estimatedSizeMb} MB",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MotionTextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun ExportOptionGroup(
    title: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MotionTextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0xFF2E2452) else MotionSurfaceVariant)
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) MotionPurplePrimary else MotionBorder,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelect(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) MotionPurplePrimary else MotionTextSecondary
                    )
                }
            }
        }
    }
}
