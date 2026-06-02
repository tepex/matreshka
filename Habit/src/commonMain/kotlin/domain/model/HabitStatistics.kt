package com.habitloop.app.habit.domain.model

data class HabitStatistics(
    val currentStreak: Int,
    val bestStreak: Int,
    val successRate30Days: Int
)
