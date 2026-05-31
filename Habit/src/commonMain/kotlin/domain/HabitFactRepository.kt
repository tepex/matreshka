package com.habitloop.app.habit.domain

import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import kotlinx.datetime.LocalDate

interface HabitFactRepository {
    fun getHabitFacts(day: LocalDate): List<HabitFact>
    /*
    fun create(day: LocalDate, habit: Habit): Result<HabitFact>
    fun update(i: Int, isCompleted: Boolean): Result<HabitFact>
    fun deleteAllByDate(day: LocalDate)
    fun deleteAll()*/
}
