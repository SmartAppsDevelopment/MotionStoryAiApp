package com.example.presentation.photoeditor

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.CropRotate
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.ShutterSpeed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import com.example.core.ui.components.FilterThumbnailCard
import com.example.core.ui.components.MotionStoryIconButton
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStorySecondaryButton
import com.example.core.ui.components.MotionStorySlider
import com.example.data.model.FilterType
import java.util.Locale

@Composable
fun PhotoEditorScreen(
    photoId: String,
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    viewModel: PhotoEditorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(photoId) {
        viewModel.loadPhoto(photoId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val photo = uiState.photo

    Scaffold(
        containerColor = MotionBgDark,
        topBar = {
            PhotoEditorTopBar(
                onBackClick = onBackClick,
                onDoneClick = { viewModel.applyChanges(onDoneClick) }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Photo Preview Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, MotionBorder, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (photo != null) {
                    Image(
                        painter = painterResource(id = photo.drawableResId),
                        contentDescription = photo.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dashed crop boundary overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )

                    // Floating info badge on bottom
                    val badgeText = when (uiState.activeTab) {
                        PhotoEditorTab.ADJUST -> "✨ Original • Photo 1"
                        PhotoEditorTab.TRANSFORM -> "✨ Crop • ${uiState.selectedCropRatio}"
                        PhotoEditorTab.FILTERS -> "✨ ${uiState.selectedFilter.title} • ${uiState.filterIntensity.toInt()}%"
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            // Bottom Editing Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MotionSurfaceDark)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    // Title
                    val panelTitle = when (uiState.activeTab) {
                        PhotoEditorTab.ADJUST -> "Adjust"
                        PhotoEditorTab.TRANSFORM -> "Transform"
                        PhotoEditorTab.FILTERS -> "Filters"
                    }
                    Text(
                        text = panelTitle,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MotionTextPrimary
                    )

                    // 3 Tabs: Adjust, Transform, Filters
                    val tabs = listOf(
                        PhotoEditorTab.ADJUST to "Adjust",
                        PhotoEditorTab.TRANSFORM to "Transform",
                        PhotoEditorTab.FILTERS to "Filters"
                    )
                    val activeIndex = tabs.indexOfFirst { it.first == uiState.activeTab }

                    TabRow(
                        selectedTabIndex = activeIndex,
                        containerColor = MotionSurfaceDark,
                        contentColor = MotionPurplePrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[activeIndex]),
                                color = MotionPurplePrimary,
                                height = 2.5.dp
                            )
                        },
                        divider = {},
                        modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                    ) {
                        tabs.forEachIndexed { index, (tab, title) ->
                            Tab(
                                selected = activeIndex == index,
                                onClick = { viewModel.selectTab(tab) },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 13.sp,
                                        fontWeight = if (activeIndex == index) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (activeIndex == index) MotionTextPrimary else MotionTextMuted
                                    )
                                }
                            )
                        }
                    }

                    // Content per tab
                    when (uiState.activeTab) {
                        PhotoEditorTab.ADJUST -> AdjustTabContent(uiState = uiState, viewModel = viewModel)
                        PhotoEditorTab.TRANSFORM -> TransformTabContent(uiState = uiState, viewModel = viewModel)
                        PhotoEditorTab.FILTERS -> FiltersTabContent(uiState = uiState, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoEditorTopBar(
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MotionStoryIconButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Edit Photo",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MotionTextPrimary
            )
            Text(
                text = "Saved",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MotionTeal
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MotionStoryIconButton(
                icon = Icons.AutoMirrored.Filled.Undo,
                contentDescription = "Undo",
                onClick = {},
                size = 36.dp
            )
            MotionStoryIconButton(
                icon = Icons.AutoMirrored.Filled.Redo,
                contentDescription = "Redo",
                onClick = {},
                size = 36.dp
            )
            Text(
                text = "Done",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MotionPurplePrimary,
                modifier = Modifier
                    .clickable(onClick = onDoneClick)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun AdjustTabContent(
    uiState: PhotoEditorUiState,
    viewModel: PhotoEditorViewModel
) {
    Column {
        // Adjust tool icons row
        val tools = listOf(
            AdjustTool.BRIGHTNESS to Icons.Default.Brightness5,
            AdjustTool.CONTRAST to Icons.Default.Contrast,
            AdjustTool.SATURATION to Icons.Default.InvertColors,
            AdjustTool.EXPOSURE to Icons.Default.ShutterSpeed,
            AdjustTool.HIGHLIGHTS to Icons.Default.LightMode,
            AdjustTool.SHADOWS to Icons.Default.Nightlight
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(tools) { (tool, icon) ->
                val isSelected = uiState.selectedAdjustTool == tool
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { viewModel.selectAdjustTool(tool) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFF2E2454) else MotionSurfaceVariant)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MotionPurplePrimary else MotionBorder,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = tool.title,
                            tint = if (isSelected) MotionPurplePrimary else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = tool.title,
                        fontSize = 11.sp,
                        color = if (isSelected) MotionPurplePrimary else MotionTextMuted,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Active value display
        val currentValue = when (uiState.selectedAdjustTool) {
            AdjustTool.BRIGHTNESS -> uiState.brightness
            AdjustTool.CONTRAST -> uiState.contrast
            AdjustTool.SATURATION -> uiState.saturation
            AdjustTool.EXPOSURE -> uiState.exposure
            AdjustTool.HIGHLIGHTS -> uiState.highlights
            AdjustTool.SHADOWS -> uiState.shadows
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = uiState.selectedAdjustTool.title,
                fontSize = 13.sp,
                color = MotionTextSecondary
            )
            Text(
                text = if (currentValue >= 0) "+${currentValue.toInt()}" else "${currentValue.toInt()}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MotionPurplePrimary
            )
        }

        // Slider
        MotionStorySlider(
            value = currentValue,
            onValueChange = { viewModel.updateCurrentAdjustValue(it) },
            valueRange = -100f..100f,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "-100", fontSize = 11.sp, color = MotionTextMuted)
            Text(text = "+100", fontSize = 11.sp, color = MotionTextMuted)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Reset and Apply buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MotionStorySecondaryButton(
                text = "Reset",
                onClick = { viewModel.resetAdjustments() },
                modifier = Modifier.weight(1f)
            )
            MotionStoryPrimaryButton(
                text = "Apply",
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TransformTabContent(
    uiState: PhotoEditorUiState,
    viewModel: PhotoEditorViewModel
) {
    Column {
        // Transform tool buttons: Crop, Rotate, Flip, Straighten
        val tools = listOf(
            "Crop" to Icons.Default.Crop,
            "Rotate" to Icons.Default.RotateRight,
            "Flip" to Icons.Default.Flip,
            "Straighten" to Icons.Default.Straighten
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tools.forEachIndexed { index, (label, icon) ->
                val isSelected = index == 0
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFF2E2454) else MotionSurfaceVariant)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MotionPurplePrimary else MotionBorder,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) MotionPurplePrimary else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = if (isSelected) MotionPurplePrimary else MotionTextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Crop ratios: Free, 9:16, 16:9, 1:1, 4:5
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val ratios = listOf("Free", "9:16", "16:9", "1:1", "4:5")
            ratios.forEach { ratio ->
                val isSelected = ratio == uiState.selectedCropRatio
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0xFF2E2452) else MotionSurfaceVariant)
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) MotionPurplePrimary else MotionBorder,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.selectCropRatio(ratio) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ratio,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) MotionPurplePrimary else MotionTextSecondary
                    )
                }
            }
        }

        // Straighten slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Straighten ${uiState.straightenDeg.toInt()}°",
                fontSize = 12.sp,
                color = MotionTextSecondary
            )
        }

        MotionStorySlider(
            value = uiState.straightenDeg,
            onValueChange = { viewModel.setStraightenDeg(it) },
            valueRange = -45f..45f,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "-45°", fontSize = 11.sp, color = MotionTextMuted)
            Text(text = "45°", fontSize = 11.sp, color = MotionTextMuted)
        }

        Spacer(modifier = Modifier.height(14.dp))

        MotionStoryPrimaryButton(
            text = "Apply",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FiltersTabContent(
    uiState: PhotoEditorUiState,
    viewModel: PhotoEditorViewModel
) {
    Column {
        // Horizontal list of filters
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            items(FilterType.entries) { filter ->
                FilterThumbnailCard(
                    filter = filter,
                    drawableResId = uiState.photo?.drawableResId ?: R.drawable.sample_lake_como,
                    isSelected = filter == uiState.selectedFilter,
                    onClick = { viewModel.selectFilter(filter) }
                )
            }
        }

        // Intensity slider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Intensity ${uiState.filterIntensity.toInt()}",
                fontSize = 13.sp,
                color = MotionTextSecondary
            )
        }

        MotionStorySlider(
            value = uiState.filterIntensity,
            onValueChange = { viewModel.setFilterIntensity(it) },
            valueRange = 0f..100f,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        MotionStoryPrimaryButton(
            text = "Apply",
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
