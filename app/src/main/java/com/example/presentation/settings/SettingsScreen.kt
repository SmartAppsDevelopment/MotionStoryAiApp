package com.example.presentation.settings

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.theme.MotionBgDark
import com.example.core.theme.MotionBorder
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.MotionStoryTopBar

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.cacheClearedMessage) {
        val msg = uiState.cacheClearedMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissMessage()
        }
    }

    Scaffold(
        containerColor = MotionBgDark,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            MotionStoryTopBar(
                title = "Settings",
                onBackClick = onBackClick
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            // Section 1: GENERAL
            SettingsSectionHeader(title = "GENERAL")
            SettingsCard {
                SettingsItemRow(
                    icon = Icons.Default.Nightlight,
                    title = "Theme",
                    value = uiState.theme,
                    onClick = {}
                )
                SettingsItemRow(
                    icon = Icons.Default.Timer,
                    title = "Default photo duration",
                    value = uiState.defaultPhotoDuration,
                    onClick = {}
                )
                SettingsItemRow(
                    icon = Icons.Default.Link,
                    title = "Default transition",
                    value = uiState.defaultTransition,
                    onClick = {}
                )
                SettingsItemRow(
                    icon = Icons.Default.Devices,
                    title = "Default aspect ratio",
                    value = uiState.defaultAspectRatio,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: EXPORT
            SettingsSectionHeader(title = "EXPORT")
            SettingsCard {
                SettingsItemRow(
                    icon = Icons.Default.Tv,
                    title = "Default resolution",
                    value = uiState.defaultResolution,
                    onClick = {}
                )
                SettingsItemRow(
                    icon = Icons.Default.Speed,
                    title = "Default FPS",
                    value = uiState.defaultFps,
                    onClick = {}
                )
                SettingsItemRow(
                    icon = Icons.Default.Verified,
                    title = "Video quality",
                    value = uiState.videoQuality,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: AUDIO
            SettingsSectionHeader(title = "AUDIO")
            SettingsCard {
                SettingsItemRow(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    title = "Default music volume",
                    value = uiState.defaultMusicVolume,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 4: STORAGE
            SettingsSectionHeader(title = "STORAGE")
            SettingsCard {
                SettingsItemRow(
                    icon = Icons.Default.CleaningServices,
                    title = "Clear cache",
                    value = uiState.cacheSize,
                    onClick = { viewModel.clearCache() }
                )
                SettingsItemRow(
                    icon = Icons.Default.Folder,
                    title = "Storage usage",
                    value = uiState.storageUsage,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 5: ABOUT
            SettingsSectionHeader(title = "ABOUT")
            SettingsCard {
                SettingsItemRow(
                    icon = Icons.Default.Security,
                    title = "Privacy Policy",
                    onClick = {}
                )
                SettingsItemRow(
                    icon = Icons.Default.Description,
                    title = "Terms",
                    onClick = {}
                )
                SettingsItemRow(
                    icon = Icons.Default.Info,
                    title = "About MotionStory",
                    value = uiState.appVersion,
                    onClick = {}
                )
                SettingsItemRow(
                    icon = Icons.Default.Star,
                    title = "Rate App",
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        color = MotionPurplePrimary,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MotionSurfaceCard)
            .border(1.dp, MotionBorder, RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Column {
            content()
        }
    }
}

@Composable
private fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MotionTextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MotionTextPrimary,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = MotionTextMuted
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MotionTextMuted,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
