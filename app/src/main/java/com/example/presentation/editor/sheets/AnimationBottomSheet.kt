package com.example.presentation.editor.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.MotionPurplePrimary
import com.example.core.theme.MotionSurfaceDark
import com.example.core.theme.MotionTextMuted
import com.example.core.theme.MotionTextPrimary
import com.example.core.ui.components.AnimationItemCard
import com.example.core.ui.components.MotionStoryPrimaryButton
import com.example.core.ui.components.MotionStorySecondaryButton
import com.example.data.model.AnimationCategory
import com.example.data.model.AnimationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationBottomSheet(
    sheetState: SheetState,
    selectedCategory: AnimationCategory,
    selectedAnimation: AnimationType,
    onCategorySelect: (AnimationCategory) -> Unit,
    onAnimationSelect: (AnimationType) -> Unit,
    onApplyToAll: () -> Unit,
    onApply: () -> Unit,
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
                text = "Photo Animation",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MotionTextPrimary
            )

            // Category Tabs: Entrance, Motion, Exit
            val categories = listOf(
                AnimationCategory.ENTRANCE to "Entrance",
                AnimationCategory.MOTION to "Motion",
                AnimationCategory.EXIT to "Exit"
            )
            val selectedIndex = categories.indexOfFirst { it.first == selectedCategory }.coerceAtLeast(0)

            TabRow(
                selectedTabIndex = selectedIndex,
                containerColor = MotionSurfaceDark,
                contentColor = MotionPurplePrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                        color = MotionPurplePrimary,
                        height = 2.5.dp
                    )
                },
                divider = {},
                modifier = Modifier.padding(top = 10.dp, bottom = 12.dp)
            ) {
                categories.forEachIndexed { index, (cat, title) ->
                    Tab(
                        selected = selectedIndex == index,
                        onClick = { onCategorySelect(cat) },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedIndex == index) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selectedIndex == index) MotionTextPrimary else MotionTextMuted
                            )
                        }
                    )
                }
            }

            // Grid of animations (4 columns)
            val animations = AnimationType.entries.filter { it.category == selectedCategory }
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                items(animations) { anim ->
                    AnimationItemCard(
                        animation = anim,
                        isSelected = anim == selectedAnimation,
                        onClick = { onAnimationSelect(anim) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MotionStorySecondaryButton(
                    text = "Apply to All",
                    onClick = onApplyToAll,
                    modifier = Modifier.weight(1f)
                )
                MotionStoryPrimaryButton(
                    text = "Apply",
                    onClick = onApply,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
