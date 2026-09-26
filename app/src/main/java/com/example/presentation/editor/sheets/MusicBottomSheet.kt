package com.example.presentation.editor.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.MotionBorder
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceDark
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTeal
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.MusicItemRow
import com.example.data.model.MusicTrack
import com.example.data.repository.FakeMusicRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
    sheetState: SheetState,
    selectedMusic: MusicTrack?,
    onSelectMusic: (MusicTrack) -> Unit,
    onOpenAudioEditor: (String) -> Unit,
    onDismiss: () -> Unit,
    musicRepository: FakeMusicRepository = remember { FakeMusicRepository() }
) {
    val tracks by musicRepository.getTracks().collectAsState(initial = emptyList())
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var playingTrackId by remember { mutableStateOf<String?>(selectedMusic?.id) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MotionSurfaceDark,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF383A4C))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Music",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MotionTextPrimary
            )

            // Tabs: Recommended, My Music, Imported, Recent
            val tabs = listOf("Recommended", "My Music", "Imported", "Recent")
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MotionSurfaceDark,
                contentColor = MotionPurplePrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MotionPurplePrimary,
                        height = 2.5.dp
                    )
                },
                divider = {},
                modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selectedTab == index) MotionTextPrimary else MotionTextMuted
                            )
                        }
                    )
                }
            }

            // Search input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Search music", color = MotionTextMuted, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MotionTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MotionSurfaceCard,
                    unfocusedContainerColor = MotionSurfaceCard,
                    focusedBorderColor = MotionPurplePrimary,
                    unfocusedBorderColor = MotionBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            // Category title & badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Made for your story",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MotionTextPrimary
                    )
                    Text(
                        text = "Royalty-free • Travel & memories",
                        fontSize = 11.sp,
                        color = MotionTextMuted
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MotionSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${tracks.size} tracks",
                        fontSize = 11.sp,
                        color = MotionTextSecondary
                    )
                }
            }

            // Track list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                items(tracks) { track ->
                    val isSelected = selectedMusic?.id == track.id
                    val isPlaying = playingTrackId == track.id
                    MusicItemRow(
                        track = track,
                        isSelected = isSelected,
                        isPlaying = isPlaying,
                        onSelect = { onSelectMusic(track) },
                        onPlayPause = {
                            playingTrackId = if (playingTrackId == track.id) null else track.id
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom row: Current selected track preview + Import Audio button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Selected track pill with tap to trim
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MotionSurfaceCard)
                        .border(1.dp, MotionBorder, RoundedCornerShape(14.dp))
                        .clickable {
                            if (selectedMusic != null) {
                                onOpenAudioEditor(selectedMusic.id)
                            }
                        }
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = MotionTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = selectedMusic?.title ?: "Select music",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MotionTextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Added • Tap to trim",
                                    fontSize = 10.sp,
                                    color = MotionTeal
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MotionTextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Import audio button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MotionSurfaceVariant)
                        .border(1.dp, MotionBorder, RoundedCornerShape(14.dp))
                        .clickable { /* Import local audio placeholder */ }
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Import Audio",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
        }
    }
}
