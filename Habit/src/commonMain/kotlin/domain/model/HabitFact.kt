package com.habitloop.app.habit.domain.model

import kotlinx.serialization.Serializable

/**
 * Value Object «Факт привычки»
 * */
@ConsistentCopyVisibility
@Serializable
data class HabitFact private constructor(
    val habitId: Habit.Id,
    val isCompleted: Boolean = false
) {

    fun switchCompletion(): HabitFact =
        HabitFact(habitId, isCompleted.not())

    fun setCompletion(isCompleted: Boolean): HabitFact =
        HabitFact(habitId, isCompleted)

    companion object {
        fun create(habitId: Habit.Id): HabitFact =
            HabitFact(habitId)
    }
}
