package com.example.presentation.export

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.core.theme.MotionPurpleDark
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceCard
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary

@Composable
fun ExportProgressScreen(
    projectId: String?,
    onExportComplete: () -> Unit,
    viewModel: ExportViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.loadProject(projectId)
        viewModel.startExportSimulation(onFinished = onExportComplete)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MotionBgDark,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Video Preview Frame with floating "Rendering frames" badge
            Box(
                modifier = Modifier
                    .width(260.dp)
                    .height(340.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .border(1.dp, MotionBorder, RoundedCornerShape(26.dp))
            ) {
                Image(
                    painter = painterResource(id = uiState.project?.coverResId ?: R.drawable.sample_lake_como),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Floating "Rendering frames" badge in center
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MotionPurplePrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Rendering frames",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Creating your video...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MotionTextPrimary
            )

            // Big Percentage Text e.g. "82%"
            Text(
                text = "${uiState.progressPercent}%",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = MotionPurplePrimary,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF262835))
            ) {
                val fraction = (uiState.progressPercent / 100f).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(MotionPurpleDark, MotionPurplePrimary, Color(0xFFC084FC))
                            )
                        )
                )
            }

            Text(
                text = uiState.progressStatusText,
                fontSize = 13.sp,
                color = MotionTextSecondary,
                modifier = Modifier.padding(top = 10.dp, bottom = 28.dp)
            )

            // Notice pill: Please don't close the app.
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(MotionSurfaceVariant)
                    .border(1.dp, MotionBorder, RoundedCornerShape(100.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Please don't close the app.",
                        fontSize = 12.sp,
                        color = MotionTextSecondary
                    )
                }
            }
        }
    }
}
