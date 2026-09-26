package com.example.presentation.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.core.theme.MotionBgDark
import com.example.core.theme.MotionPurpleBadge
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceDark
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.GalleryPhotoCard
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStoryTopBar

@Composable
fun SelectPhotosScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    viewModel: SelectPhotosViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCount = uiState.selectedPhotos.size
    val canContinue = selectedCount >= 1

    Scaffold(
        containerColor = MotionBgDark,
        topBar = {
            MotionStoryTopBar(
                title = "Select Photos",
                onBackClick = onBackClick,
                actionText = "Next",
                onActionClick = onContinueClick,
                actionEnabled = canContinue
            )
        },
        bottomBar = {
            if (canContinue) {
                SelectedPhotosBottomBar(
                    selectedCount = selectedCount,
                    selectedPhotos = uiState.selectedPhotos,
                    onRemovePhoto = { viewModel.removePhoto(it) },
                    onContinue = onContinueClick
                )
            } else {
                EmptySelectionBottomBar()
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Count and subtitle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$selectedCount selected",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MotionTextPrimary
                )
                Text(
                    text = "Choose at least 2 photos",
                    fontSize = 12.sp,
                    color = MotionTextMuted
                )
            }

            // Categories tabs: Photos, Albums, Recent, Favorites
            val tabs = listOf("Photos", "Albums", "Recent", "Favorites")
            TabRow(
                selectedTabIndex = uiState.selectedTab,
                containerColor = MotionBgDark,
                contentColor = MotionPurplePrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                        color = MotionPurplePrimary,
                        height = 2.5.dp
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTab == index,
                        onClick = { viewModel.selectTab(index) },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (uiState.selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (uiState.selectedTab == index) MotionTextPrimary else MotionTextMuted
                            )
                        }
                    )
                }
            }

            // Month section header: September
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "September",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MotionTextPrimary
                )
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter",
                    tint = MotionTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // 3-column photo grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.availablePhotos, key = { it.id }) { photo ->
                    val indexInSelected = uiState.selectedPhotos.indexOfFirst { it.id == photo.id }
                    val isSelected = indexInSelected >= 0
                    GalleryPhotoCard(
                        photo = photo,
                        isSelected = isSelected,
                        selectionIndex = if (isSelected) indexInSelected + 1 else 0,
                        onClick = { viewModel.togglePhoto(photo.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedPhotosBottomBar(
    selectedCount: Int,
    selectedPhotos: List<com.example.data.model.PhotoItem>,
    onRemovePhoto: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(MotionSurfaceDark)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Text(
                text = "Selected Photos",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MotionTextPrimary
            )
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C2448)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = selectedCount.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MotionPurplePrimary
                )
            }
        }

        // Horizontal Strip of selected thumbnails with X
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            items(selectedPhotos, key = { it.id }) { photo ->
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = painterResource(id = photo.drawableResId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // (X) button
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .clickable { onRemovePhoto(photo.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
        }

        MotionStoryPrimaryButton(
            text = "Continue",
            onClick = onContinue,
            leadingIcon = Icons.AutoMirrored.Filled.ArrowForward,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EmptySelectionBottomBar() {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .background(MotionBgDark)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.PhotoLibrary,
            contentDescription = null,
            tint = MotionTextMuted,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = "Tap photos to add them to your story",
            fontSize = 13.sp,
            color = MotionTextMuted,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )
        MotionStoryPrimaryButton(
            text = "Continue",
            onClick = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
