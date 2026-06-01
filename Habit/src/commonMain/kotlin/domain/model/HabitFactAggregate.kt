package com.habitloop.app.habit.domain.model

@ConsistentCopyVisibility
data class HabitFactAggregate private constructor(
    val fact: HabitFact,
    val habitData: Habit.Data
) {
    companion object {
        fun create(fact: HabitFact, habitData: Habit.Data): HabitFactAggregate =
            HabitFactAggregate(fact, habitData)

        fun create(habit: Habit): HabitFactAggregate =
            HabitFactAggregate(HabitFact.create(habit.id), habit.data)
    }
}
