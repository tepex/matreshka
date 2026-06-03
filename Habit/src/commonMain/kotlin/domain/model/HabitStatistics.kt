package com.habitloop.app.habit.domain.model

data class HabitStatistics(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val successRate30Days: Int = 0
)
