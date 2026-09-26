package com.example.presentation.editor.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.MotionSurfaceDark
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.theme.MotionTextSecondary
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStorySecondaryButton
import com.example.core.ui.components.MotionStorySlider
import com.example.core.ui.components.TransitionItemCard
import com.example.data.model.TransitionType
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitionBottomSheet(
    sheetState: SheetState,
    selectedTransition: TransitionType,
    transitionDuration: Float,
    onTransitionSelect: (TransitionType) -> Unit,
    onDurationChange: (Float) -> Unit,
    onApplyToCurrent: () -> Unit,
    onApplyToAll: () -> Unit,
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
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Transition",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MotionTextPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Horizontal row of transition cards
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(TransitionType.entries) { transition ->
                    TransitionItemCard(
                        transition = transition,
                        isSelected = transition == selectedTransition,
                        onClick = { onTransitionSelect(transition) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transition duration",
                    fontSize = 13.sp,
                    color = MotionTextSecondary
                )
                Text(
                    text = String.format(Locale.US, "%.1f sec", transitionDuration),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MotionTextPrimary
                )
            }

            // Duration Slider
            MotionStorySlider(
                value = transitionDuration,
                onValueChange = onDurationChange,
                valueRange = 0.1f..2.0f,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "0.1", fontSize = 11.sp, color = MotionTextMuted)
                Text(text = "2.0", fontSize = 11.sp, color = MotionTextMuted)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MotionStorySecondaryButton(
                    text = "Apply to Current",
                    onClick = onApplyToCurrent,
                    modifier = Modifier.weight(1f)
                )
                MotionStoryPrimaryButton(
                    text = "Apply to All",
                    onClick = onApplyToAll,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
