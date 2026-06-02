package com.habitloop.app.habit.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.habitloop.app.habit.domain.model.Habit

data class HabitItem(
    val id: Habit.Id,
    val name: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val isCompleted: Boolean
)
