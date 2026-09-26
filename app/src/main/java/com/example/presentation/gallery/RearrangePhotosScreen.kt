package com.example.presentation.gallery

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStorySecondaryButton
import com.example.core.ui.components.MotionStoryTopBar
import com.example.data.model.PhotoItem

@Composable
fun RearrangePhotosScreen(
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    viewModel: RearrangePhotosViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val photos by viewModel.photos.collectAsState()

    Scaffold(
        containerColor = MotionBgDark,
        topBar = {
            MotionStoryTopBar(
                title = "Rearrange Photos",
                onBackClick = onBackClick,
                actionText = "Done",
                onActionClick = { viewModel.saveReorder(onDoneClick) }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MotionStorySecondaryButton(
                    text = "Cancel",
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                )
                MotionStoryPrimaryButton(
                    text = "Done",
                    onClick = { viewModel.saveReorder(onDoneClick) },
                    modifier = Modifier.weight(1f)
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
            // Instruction
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.OpenWith,
                    contentDescription = null,
                    tint = MotionPurplePrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Press arrows or drag to change the order",
                    fontSize = 13.sp,
                    color = MotionTextSecondary
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(photos, key = { _, item -> item.id }) { index, photo ->
                    RearrangeItemRow(
                        photo = photo,
                        canMoveUp = index > 0,
                        canMoveDown = index < photos.size - 1,
                        onMoveUp = { viewModel.moveItem(index, index - 1) },
                        onMoveDown = { viewModel.moveItem(index, index + 1) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun RearrangeItemRow(
    photo: PhotoItem,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MotionSurfaceCard)
            .border(1.dp, MotionBorder, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sequence number badge
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFF2C2548)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = photo.order.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MotionPurplePrimary
            )
        }

        // Photo thumbnail
        Image(
            painter = painterResource(id = photo.drawableResId),
            contentDescription = photo.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(width = 68.dp, height = 48.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        // Title and duration
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp)
        ) {
            Text(
                text = photo.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MotionTextPrimary
            )
            Text(
                text = "${photo.durationSeconds} sec",
                fontSize = 12.sp,
                color = MotionTextMuted,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Reorder arrows
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (canMoveUp) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Move Up",
                    tint = MotionTextSecondary,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(onClick = onMoveUp)
                )
            }
            if (canMoveDown) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Move Down",
                    tint = MotionTextSecondary,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(onClick = onMoveDown)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.DragHandle,
            contentDescription = "Drag",
            tint = MotionTextMuted,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )
    }
}
