package com.habitloop.app.habit.data

import com.habitloop.app.habit.domain.Habit
import com.habitloop.app.habit.domain.HabitDto
import com.habitloop.app.habit.domain.HabitRepository
import kotlinx.datetime.LocalDate

class HabitRepositoryImpl(private val storage: HabitStorage) : HabitRepository {

    override fun getHabits(): List<Habit> =
        //storage.getRawHabits().ifEmpty { defaultHabits }
        storage.getRawHabits()

    override fun addHabit(dto: HabitDto): Habit {
        val currentHabits = storage.getRawHabits().toMutableList()

        // Генерируем следующий ID на основе распакованного int-значения
        val nextIdValue = (currentHabits.maxOfOrNull { it.id.value } ?: 0) + 1

        val newHabit = Habit(
            id = Habit.Id(nextIdValue),
            name = dto.name,
            description = dto.description,
            type = dto.type,
            createdDate = dto.createdDate,
            false
        )

        currentHabits.add(newHabit)
        storage.saveRawHabits(currentHabits)
        return newHabit
    }

    override fun isHabitCompleted(habitId: Habit.Id, date: LocalDate): Boolean {
        // Формируем уникальный ключ
        val historyKey = "${date}_${habitId.value}"
        return storage.getHistoryMap()[historyKey] ?: false
    }

    override fun toggleHabitCompletion(habitId: Habit.Id, date: LocalDate) {
        val historyKey = "${date}_${habitId.value}"
        val currentHistory = storage.getHistoryMap().toMutableMap()

        val currentStatus = currentHistory[historyKey] ?: false
        currentHistory[historyKey] = !currentStatus

        storage.saveHistoryMap(currentHistory)
    }
}
