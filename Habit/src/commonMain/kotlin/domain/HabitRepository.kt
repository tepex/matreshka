package com.habitloop.app.habit.domain

import kotlinx.datetime.LocalDate

interface HabitRepository {
    fun getHabits(): List<Habit>

    fun addHabit(dto: HabitDto): Habit

    fun isHabitCompleted(habitId: Habit.Id, date: LocalDate): Boolean

    fun toggleHabitCompletion(habitId: Habit.Id, date: LocalDate)
}
