package com.habitloop.app.habit.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class HabitItem(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val isCompleted: Boolean
)
