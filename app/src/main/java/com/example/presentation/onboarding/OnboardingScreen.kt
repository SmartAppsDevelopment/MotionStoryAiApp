package com.example.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.core.theme.MotionBgDark
import com.example.core.theme.MotionBorder
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.AudioWaveformView
import com.example.core.ui.components.MotionStoryPrimaryButton

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    viewModel: OnboardingViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentStep by viewModel.currentStep.collectAsState()

    Scaffold(
        containerColor = MotionBgDark,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: MotionStory logo & Skip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MotionStory",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MotionTextPrimary
                )
                Text(
                    text = "Skip",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MotionTextSecondary,
                    modifier = Modifier.clickable(onClick = onFinishOnboarding)
                )
            }

            // Visual Center Illustration for current step
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (currentStep) {
                    0 -> OnboardingStepOneVisual()
                    1 -> OnboardingStepTwoVisual()
                    else -> OnboardingStepThreeVisual()
                }
            }

            // Bottom Content: Title, Description, Dots, Next Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                val title = when (currentStep) {
                    0 -> "Turn Photos Into Videos"
                    1 -> "Make Every Photo Beautiful"
                    else -> "Add Music & Share"
                }

                val desc = when (currentStep) {
                    0 -> "Choose your favorite moments and watch them flow into a beautiful, perfectly timed story."
                    1 -> "Polish every frame with thoughtful filters, adjustments, motion, and cinematic animation."
                    else -> "Set the mood with music, preview your final video, and share it anywhere in a tap."
                }

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MotionTextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = desc,
                    fontSize = 14.sp,
                    color = MotionTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 10.dp, bottom = 24.dp)
                )

                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (index == currentStep) 24.dp else 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (index == currentStep) MotionPurplePrimary else Color(0xFF2C2E3C)
                                )
                        )
                    }
                }

                // Action Button
                if (currentStep < 2) {
                    MotionStoryPrimaryButton(
                        text = "Next",
                        onClick = { viewModel.nextStep() },
                        leadingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    MotionStoryPrimaryButton(
                        text = "Start Creating",
                        onClick = onFinishOnboarding,
                        leadingIcon = Icons.Default.AutoAwesome,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun OnboardingStepOneVisual() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(280.dp)
                .height(260.dp)
                .clip(RoundedCornerShape(22.dp))
                .border(2.dp, MotionPurplePrimary.copy(alpha = 0.6f), RoundedCornerShape(22.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.sample_lake_como),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Thumbnail strip
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MotionSurfaceVariant)
                .padding(8.dp)
        ) {
            val thumbnails = listOf(
                R.drawable.sample_lake_como,
                R.drawable.sample_cove,
                R.drawable.sample_beach,
                R.drawable.sample_cliff_town
            )
            thumbnails.forEach { resId ->
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
private fun OnboardingStepTwoVisual() {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(340.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(2.dp, MotionPurplePrimary.copy(alpha = 0.7f), RoundedCornerShape(24.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.sample_cove),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Floating pill on bottom of photo
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "✨ Cinematic + Ken Burns",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        // 3 floating tool buttons below the pill
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        ) {
            listOf(
                Icons.Default.Tune,
                Icons.Default.ColorLens,
                Icons.Default.AutoAwesome
            ).forEach { icon ->
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF222432).copy(alpha = 0.9f))
                        .border(1.dp, MotionBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingStepThreeVisual() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(270.dp)
                .height(260.dp)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.sample_beach),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Play button overlay
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Share bubble badge top right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MotionPurplePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Purple audio waveform preview
        AudioWaveformView(
            modifier = Modifier.width(270.dp),
            trimStartFrac = 0.1f,
            trimEndFrac = 0.75f
        )
    }
}
