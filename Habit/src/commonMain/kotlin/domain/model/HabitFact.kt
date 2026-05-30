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

    companion object {
        fun create(habitId: Habit.Id): HabitFact =
            HabitFact(habitId)
    }
}
