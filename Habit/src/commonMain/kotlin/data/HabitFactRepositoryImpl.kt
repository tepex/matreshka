package com.habitloop.app.habit.data

import com.habitloop.app.habit.domain.HabitFactRepository
import com.habitloop.app.habit.domain.model.HabitFact
import kotlinx.datetime.LocalDate

class HabitFactRepositoryImpl : HabitFactRepository {
    override fun getHabitFacts(day: LocalDate): List<HabitFact> {
        return emptyList()
    }
}
