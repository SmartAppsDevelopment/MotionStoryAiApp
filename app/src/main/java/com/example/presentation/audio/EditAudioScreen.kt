package com.example.presentation.audio

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.AudioWaveformView
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStorySlider
import com.example.core.ui.components.MotionStoryTopBar

@Composable
fun EditAudioScreen(
    trackId: String,
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit,
    viewModel: EditAudioViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(trackId) {
        viewModel.loadTrack(trackId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val track = uiState.track

    Scaffold(
        containerColor = MotionBgDark,
        topBar = {
            MotionStoryTopBar(
                title = "Edit Audio",
                onBackClick = onBackClick,
                actionText = "Apply",
                onActionClick = { viewModel.applySettings(onApplyClick) }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                MotionStoryPrimaryButton(
                    text = "Apply",
                    onClick = { viewModel.applySettings(onApplyClick) },
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
                .verticalScroll(rememberScrollState())
        ) {
            // Preview image with play button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, MotionBorder, RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sample_lake_como),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Play / Pause center circle
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(MotionPurplePrimary)
                        .clickable { viewModel.togglePlayPause() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Track info: Title, Artist • duration, and "Selected" green pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = track?.title ?: "Sunset Drive",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MotionTextPrimary
                    )
                    Text(
                        text = "${track?.artist ?: "Maya Woods"} • ${track?.durationFormatted ?: "2:48"}",
                        fontSize = 13.sp,
                        color = MotionTextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MotionTeal.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Selected",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MotionTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Waveform with trim handles
            AudioWaveformView(
                modifier = Modifier.fillMaxWidth(),
                trimStartFrac = 0.15f,
                trimEndFrac = 0.82f
            )

            // Trim start & end labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Start 00:12.4",
                    fontSize = 12.sp,
                    color = MotionTextSecondary
                )
                Text(
                    text = "End 00:30.4 • 18 sec",
                    fontSize = 12.sp,
                    color = MotionTextSecondary
                )
            }

            // Toggles: Fade In & Fade Out
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AudioToggleCard(
                    title = "Fade In",
                    checked = uiState.fadeIn,
                    onCheckedChange = { viewModel.setFadeIn(it) },
                    modifier = Modifier.weight(1f)
                )
                AudioToggleCard(
                    title = "Fade Out",
                    checked = uiState.fadeOut,
                    onCheckedChange = { viewModel.setFadeOut(it) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Music Volume Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Music Volume",
                    fontSize = 13.sp,
                    color = MotionTextSecondary
                )
                Text(
                    text = "${uiState.musicVolume.toInt()}%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MotionPurplePrimary
                )
            }
            MotionStorySlider(
                value = uiState.musicVolume,
                onValueChange = { viewModel.setMusicVolume(it) },
                valueRange = 0f..100f
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Original Audio Volume Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Original Audio Volume",
                    fontSize = 13.sp,
                    color = MotionTextSecondary
                )
                Text(
                    text = "${uiState.originalAudioVolume.toInt()}%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MotionPurplePrimary
                )
            }
            MotionStorySlider(
                value = uiState.originalAudioVolume,
                onValueChange = { viewModel.setOriginalAudioVolume(it) },
                valueRange = 0f..100f
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AudioToggleCard(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MotionSurfaceCard)
            .border(1.dp, MotionBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MotionTextPrimary
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MotionPurplePrimary,
                    uncheckedThumbColor = Color(0xFFA1A3B5),
                    uncheckedTrackColor = Color(0xFF2C2E3C)
                )
            )
        }
    }
}
