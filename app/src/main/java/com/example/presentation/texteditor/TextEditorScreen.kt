package com.example.presentation.texteditor

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.theme.MotionBgDark
import com.example.core.theme.MotionBorder
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceDark
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStorySecondaryButton
import com.example.core.ui.components.MotionStoryTopBar

@Composable
fun TextEditorScreen(
    photoId: String,
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit,
    viewModel: TextEditorViewModel = viewModel(),
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
            MotionStoryTopBar(
                title = "Add Text",
                onBackClick = onBackClick,
                actionText = "Apply",
                onActionClick = { viewModel.applyText(onApplyClick) }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MotionSurfaceDark)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Input text field with clear button
                OutlinedTextField(
                    value = uiState.text,
                    onValueChange = { viewModel.updateText(it) },
                    placeholder = { Text("Type something...", color = MotionTextMuted) },
                    trailingIcon = {
                        if (uiState.text.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = MotionTextMuted,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { viewModel.updateText("") }
                            )
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MotionSurfaceCard,
                        unfocusedContainerColor = MotionSurfaceCard,
                        focusedBorderColor = MotionPurplePrimary,
                        unfocusedBorderColor = MotionBorder,
                        focusedTextColor = MotionTextPrimary,
                        unfocusedTextColor = MotionTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Text Formatting Tools Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextToolItem(
                        icon = Icons.Default.TextFields,
                        label = "Font",
                        onClick = {}
                    )
                    TextToolItem(
                        icon = Icons.Default.FormatSize,
                        label = "Size",
                        onClick = {}
                    )
                    TextToolItem(
                        icon = Icons.Default.ColorLens,
                        label = "Color",
                        onClick = {},
                        iconTint = MotionPurplePrimary
                    )
                    TextToolItem(
                        icon = Icons.Default.FormatAlignCenter,
                        label = "Alignment",
                        onClick = {}
                    )
                    TextToolItem(
                        icon = Icons.Default.FormatBold,
                        label = "Bold",
                        isSelected = uiState.isBold,
                        onClick = { viewModel.toggleBold() }
                    )
                    TextToolItem(
                        icon = Icons.Default.FormatItalic,
                        label = "Italic",
                        isSelected = uiState.isItalic,
                        onClick = { viewModel.toggleItalic() }
                    )
                    TextToolItem(
                        icon = Icons.Default.InvertColors,
                        label = "Background",
                        isSelected = uiState.hasBackground,
                        onClick = { viewModel.toggleBackground() }
                    )
                    TextToolItem(
                        icon = Icons.Default.OpenWith,
                        label = "Position",
                        onClick = {}
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Text Animation selection
                Text(
                    text = "Text animation",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MotionTextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val animations = listOf("Fade", "Slide", "Typewriter", "Zoom", "Bounce")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(animations) { anim ->
                        val isSelected = anim == uiState.selectedAnimation
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xFF2E2254) else MotionSurfaceVariant)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) MotionPurplePrimary else MotionBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.selectAnimation(anim) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = anim,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) MotionPurplePrimary else MotionTextSecondary
                            )
                        }
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MotionStorySecondaryButton(
                        text = "Cancel",
                        onClick = onBackClick,
                        modifier = Modifier.weight(1f)
                    )
                    MotionStoryPrimaryButton(
                        text = "Apply",
                        onClick = { viewModel.applyText(onApplyClick) },
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Preview card with text overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, MotionBorder, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (photo != null) {
                    Image(
                        painter = painterResource(id = photo.drawableResId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Dashed guide border
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

                    // Text overlay in center
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (uiState.hasBackground) Color.Black.copy(alpha = 0.6f) else Color.Transparent
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.text.ifBlank { "Type something..." },
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = if (uiState.isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (uiState.isItalic) FontStyle.Italic else FontStyle.Normal
                        )
                    }

                    // Floating badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "✨ Text • ${uiState.selectedAnimation} in",
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TextToolItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    iconTint: Color? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFF2E2254) else MotionSurfaceVariant)
                .border(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) MotionPurplePrimary else MotionBorder,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint ?: if (isSelected) MotionPurplePrimary else Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) MotionPurplePrimary else MotionTextMuted,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
