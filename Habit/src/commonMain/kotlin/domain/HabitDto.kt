package com.habitloop.app.habit.domain

import kotlinx.datetime.LocalDate

data class HabitDto(
    val name: Habit.Name,
    val description: Habit.Description,
    val type: Habit.Type,
    val typeColor: Habit.TypeColor,
    val createdDate: LocalDate
)
