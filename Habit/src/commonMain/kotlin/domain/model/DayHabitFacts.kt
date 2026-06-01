package com.habitloop.app.habit.domain.model

import kotlinx.datetime.LocalDate

/**
 * Агрегат «Факты привычек дня»
 *
 * */
class DayHabitFacts(
    // Identity
    //val day: LocalDate,
    habitFacts: List<HabitFact>
) {

    private val _habitFacts = mutableListOf<HabitFact>()

    init {

    }
}
