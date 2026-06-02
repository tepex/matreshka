package com.habitloop.app.habit.domain

import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import kotlinx.datetime.LocalDate

interface HabitFactRepository {
    fun getHabitFacts(day: LocalDate): List<HabitFact>

    fun add(day: LocalDate, fact: HabitFact): Result<HabitFact>

    fun addAll(day: LocalDate, facts: List<HabitFact>): Result<List<HabitFact>>

    fun update(day: LocalDate, i: Int, fact: HabitFact): Result<HabitFact>
    /*
    fun create(day: LocalDate, habit: Habit): Result<HabitFact>

    fun deleteAllByDate(day: LocalDate)
    fun deleteAll()*/
}
