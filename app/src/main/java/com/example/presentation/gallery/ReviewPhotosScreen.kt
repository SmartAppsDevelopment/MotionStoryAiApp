package com.example.presentation.gallery

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.core.theme.MotionBorder
import com.example.core.theme.MotionPurpleBadge
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceDark
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTeal
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStorySecondaryButton
import com.example.core.ui.components.MotionStoryTopBar
import com.example.data.model.PhotoItem

@Composable
fun ReviewPhotosScreen(
    onBackClick: () -> Unit,
    onAddMorePhotos: () -> Unit,
    onRearrangeClick: () -> Unit,
    onContinueEditing: () -> Unit,
    onCreateVideo: () -> Unit,
    viewModel: ReviewPhotosViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MotionBgDark,
        topBar = {
            MotionStoryTopBar(
                title = "Your Photos",
                onBackClick = onBackClick,
                trailingContent = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MotionSurfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "${uiState.photos.size} photos",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MotionTextSecondary
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Add more photos button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, MotionBorder, RoundedCornerShape(16.dp))
                        .background(MotionSurfaceVariant)
                        .clickable(onClick = onAddMorePhotos),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Add More Photos",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                // Row of two action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MotionStorySecondaryButton(
                        text = "Continue Editing",
                        onClick = onContinueEditing,
                        modifier = Modifier.weight(1f)
                    )
                    MotionStoryPrimaryButton(
                        text = "Create Video",
                        onClick = onCreateVideo,
                        leadingIcon = Icons.Default.AutoAwesome,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Drag photos hint
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onRearrangeClick)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Drag photos to change their order",
                    fontSize = 13.sp,
                    color = MotionTextSecondary
                )
                Text(
                    text = "Reorder",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MotionPurplePrimary
                )
            }

            // Large cards horizontal carousel
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(uiState.photos, key = { it.id }) { photo ->
                    LargePhotoReviewCard(
                        photo = photo,
                        onRemove = { viewModel.removePhoto(photo.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Story sequence card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MotionSurfaceCard)
                    .border(1.dp, MotionBorder, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Story sequence",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MotionTextPrimary
                        )
                        Text(
                            text = uiState.totalDurationFormatted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MotionTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.photos, key = { it.id }) { photo ->
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = photo.drawableResId),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(3.dp)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .clickable { viewModel.removePhoto(photo.id) },
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
                }
            }
        }
    }
}

@Composable
private fun LargePhotoReviewCard(
    photo: PhotoItem,
    onRemove: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(240.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(2.dp, MotionPurplePrimary.copy(alpha = 0.8f), RoundedCornerShape(18.dp))
        ) {
            Image(
                painter = painterResource(id = photo.drawableResId),
                contentDescription = photo.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Sequence Badge (1, 2, 3...)
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(MotionPurpleBadge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = photo.order.toString(),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Close button (X)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .width(160.dp)
                .padding(top = 8.dp, start = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${photo.durationSeconds} sec",
                fontSize = 12.sp,
                color = MotionTextSecondary
            )
            Icon(
                imageVector = Icons.Default.DragHandle,
                contentDescription = "Drag handle",
                tint = MotionTextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
