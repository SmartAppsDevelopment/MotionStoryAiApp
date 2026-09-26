package com.example.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.MotionBorder
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionRed
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.data.model.FilterType

@Composable
fun MotionStorySlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = MotionPurplePrimary,
            inactiveTrackColor = Color(0xFF2B2D3B)
        ),
        modifier = modifier
    )
}

@Composable
fun FilterThumbnailCard(
    filter: FilterType,
    drawableResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(76.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = if (isSelected) 2.5.dp else 1.dp,
                    color = if (isSelected) MotionPurplePrimary else MotionBorder,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Image(
                painter = painterResource(id = drawableResId),
                contentDescription = filter.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Color overlay tint based on filter
            val tint = when (filter) {
                FilterType.ORIGINAL -> Color.Transparent
                FilterType.VIVID -> Color(0x22FFA500)
                FilterType.WARM -> Color(0x33FF8C00)
                FilterType.COOL -> Color(0x3300BFFF)
                FilterType.VINTAGE -> Color(0x338B7355)
                FilterType.CINEMATIC -> Color(0x261E90FF)
                FilterType.BW -> Color(0x55000000)
                FilterType.DRAMATIC -> Color(0x444A0E4E)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(tint)
            )
        }
        Text(
            text = filter.title,
            fontSize = 11.sp,
            color = if (isSelected) MotionPurplePrimary else MotionTextSecondary,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun AudioWaveformView(
    modifier: Modifier = Modifier,
    trimStartFrac: Float = 0.15f,
    trimEndFrac: Float = 0.85f
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF14151E))
            .border(1.dp, MotionBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Draw decorative realistic waveform bars
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barCount = 38
            val barWidth = 4.dp.toPx()
            val totalWidth = size.width
            val spacing = (totalWidth - (barCount * barWidth)) / (barCount - 1)
            val maxHeight = size.height * 0.8f

            val amplitudes = listOf(
                0.2f, 0.4f, 0.8f, 0.3f, 0.6f, 0.95f, 0.5f, 0.7f, 0.85f, 0.4f,
                0.9f, 0.6f, 0.75f, 0.45f, 0.6f, 0.9f, 0.7f, 0.35f, 0.8f, 0.5f,
                0.65f, 0.85f, 0.4f, 0.7f, 0.95f, 0.6f, 0.3f, 0.8f, 0.5f, 0.4f,
                0.7f, 0.6f, 0.45f, 0.35f, 0.55f, 0.4f, 0.3f, 0.2f
            )

            for (i in 0 until barCount) {
                val amp = amplitudes[i % amplitudes.size]
                val barHeight = maxHeight * amp
                val x = i * (barWidth + spacing)
                val y = (size.height - barHeight) / 2

                val progressFrac = i.toFloat() / barCount
                val isInsideTrim = progressFrac in trimStartFrac..trimEndFrac

                val color = if (isInsideTrim) MotionPurplePrimary else Color(0xFF2C2D3E)
                drawRoundRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
                )
            }

            // Draw white trim handles
            val handleWidth = 4.dp.toPx()
            val startX = totalWidth * trimStartFrac
            val endX = totalWidth * trimEndFrac

            // Start handle
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(startX, 0f),
                size = Size(handleWidth, size.height),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            // End handle
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(endX, 0f),
                size = Size(handleWidth, size.height),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
        }
    }
}

@Composable
fun DeleteProjectDialog(
    projectTitle: String,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete project?",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MotionTextPrimary
            )
        },
        text = {
            Text(
                text = "This can't be undone.",
                fontSize = 14.sp,
                color = MotionTextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmDelete) {
                Text(
                    text = "Delete",
                    color = MotionRed,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = MotionTextSecondary
                )
            }
        },
        containerColor = MotionSurfaceCard,
        shape = RoundedCornerShape(18.dp)
    )
}
