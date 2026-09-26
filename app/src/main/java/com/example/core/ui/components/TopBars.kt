package com.example.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.MotionBgDark
import com.example.core.theme.MotionDimens
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceVariant
import com.example.core.theme.MotionTeal
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary

@Composable
fun MotionStoryTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    actionEnabled: Boolean = true,
    trailingContent: (@Composable () -> Unit)? = null,
    testTag: String = "top_bar"
) {
    Row(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .height(MotionDimens.topBarHeight)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (onBackClick != null) {
                MotionStoryIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    onClick = onBackClick,
                    testTag = "top_bar_back_button"
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MotionTextPrimary
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MotionTeal,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (actionText != null && onActionClick != null) {
                Text(
                    text = actionText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (actionEnabled) MotionPurplePrimary else MotionTextMuted,
                    modifier = Modifier
                        .clickable(enabled = actionEnabled, onClick = onActionClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )
            } else if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}

@Composable
fun EditorTopBar(
    title: String,
    onBackClick: () -> Unit,
    onPreviewClick: () -> Unit,
    onUndoClick: () -> Unit = {},
    onRedoClick: () -> Unit = {},
    statusText: String = "Saved",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(MotionDimens.topBarHeight)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MotionStoryIconButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick,
            testTag = "editor_back_button"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MotionTextPrimary
            )
            Text(
                text = statusText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MotionTeal
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MotionStoryIconButton(
                icon = Icons.AutoMirrored.Filled.Undo,
                contentDescription = "Undo",
                onClick = onUndoClick,
                size = 36.dp
            )
            MotionStoryIconButton(
                icon = Icons.AutoMirrored.Filled.Redo,
                contentDescription = "Redo",
                onClick = onRedoClick,
                size = 36.dp
            )

            // Preview pill button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF2C254A))
                    .clickable(onClick = onPreviewClick)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Preview",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MotionPurplePrimary
                )
            }
        }
    }
}
