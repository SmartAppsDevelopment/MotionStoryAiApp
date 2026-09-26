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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStorySlider
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationBottomSheet(
    sheetState: SheetState,
    currentDuration: Float,
    applyToAll: Boolean,
    totalPhotosCount: Int,
    onDurationChange: (Float) -> Unit,
    onApplyToAllChange: (Boolean) -> Unit,
    onDone: () -> Unit,
    onDismiss: () -> Unit
) {
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
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Photo Duration",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MotionTextPrimary
                )
            }

            // Big Duration display
            Text(
                text = String.format(Locale.US, "%.1f sec", currentDuration),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MotionTextPrimary,
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
            )

            // Slider
            MotionStorySlider(
                value = currentDuration,
                onValueChange = onDurationChange,
                valueRange = 0.5f..10.0f,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "0.5", fontSize = 11.sp, color = MotionTextMuted)
                Text(text = "10\nsec", fontSize = 11.sp, color = MotionTextMuted)
            }

            // Preset value chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val presets = listOf(0.5f, 1.0f, 1.5f, 2.0f, 3.0f, 5.0f)
                presets.forEach { preset ->
                    val isSelected = kotlin.math.abs(preset - currentDuration) < 0.15f
                    val label = if (preset % 1.0f == 0.0f) "${preset.toInt()}s" else "${preset}s"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) Color(0xFF2E2452) else MotionSurfaceVariant)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MotionPurplePrimary else MotionBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onDurationChange(preset) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) MotionPurplePrimary else MotionTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Apply to all photos toggle card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
                    Column {
                        Text(
                            text = "Apply to all photos",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MotionTextPrimary
                        )
                        Text(
                            text = String.format(Locale.US, "Set all %d photos to %.1f sec", totalPhotosCount, currentDuration),
                            fontSize = 11.sp,
                            color = MotionTextMuted,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Switch(
                        checked = applyToAll,
                        onCheckedChange = onApplyToAllChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MotionPurplePrimary,
                            uncheckedThumbColor = Color(0xFFA1A3B5),
                            uncheckedTrackColor = Color(0xFF2C2E3C)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total video time info
            val calculatedTotal = if (applyToAll) totalPhotosCount * currentDuration else 12.5f
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total video",
                    fontSize = 13.sp,
                    color = MotionTextSecondary
                )
                Text(
                    text = String.format(Locale.US, "%.1f sec", calculatedTotal),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MotionTeal
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Done button
            MotionStoryPrimaryButton(
                text = "Done",
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
        }
    }
}
