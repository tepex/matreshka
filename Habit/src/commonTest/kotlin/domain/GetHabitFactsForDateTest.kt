package com.habitloop.app.habit.domain

import com.habitloop.app.habit.data.HabitFactRepositoryImpl
import com.habitloop.app.habit.data.HabitRepositoryImpl
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class GetHabitFactsForDateTest : FunSpec({

    // 1 марта 2026 года — это Воскресенье.
    val today = LocalDate(2026, 3, 1)
    // 28 февраля 2026 года — это Суббота (ordinal = 5)
    val yesterday = LocalDate(2026, 2, 28)

    test("should return existing facts from repository for today") {
        val habitFactRepository = HabitFactRepositoryImpl()
        val habitRepository = HabitRepositoryImpl()

        // Используем точное имя параметра weekIndices
        Habit.create(
            name = "Run",
            icon = Habit.Data.IconType.ICON1,
            color = Habit.Data.ColorType.COLOR1,
            weekIndices = setOf(today.dayOfWeek.ordinal)
        ).also { habit ->
            habitRepository.create(habit)
            habitFactRepository.add(today, HabitFact.create(habitId = habit.id).setCompletion(true))

            getHabitFactsForDate(today, today, habitFactRepository, habitRepository).run {
                size shouldBe 1
                first().fact.habitId shouldBe habit.id
                first().fact.isCompleted shouldBe true
            }
        }
    }

    test("should create and record a new fact if it is missing for today") {
        val habitFactRepository = HabitFactRepositoryImpl()
        val habitRepository = HabitRepositoryImpl()

        Habit.create(
            name = "Read",
            icon = Habit.Data.IconType.ICON3,
            color = Habit.Data.ColorType.COLOR3,
            weekIndices = setOf(today.dayOfWeek.ordinal)
        ).also { habit ->
            habitRepository.create(habit)

            getHabitFactsForDate(today, today, habitFactRepository, habitRepository).run {
                size shouldBe 1
                first().fact.habitId shouldBe habit.id
                first().fact.isCompleted shouldBe false
            }

            // Проверяем автоматическую запись факта
            habitFactRepository.getHabitFacts(today).run {
                size shouldBe 1
                first().habitId shouldBe habit.id
            }
        }
    }

    test("should return empty list if habit is not scheduled for that day of week") {
        val habitFactRepository = HabitFactRepositoryImpl()
        val habitRepository = HabitRepositoryImpl()

        Habit.create(
            name = "Gym",
            icon = Habit.Data.IconType.ICON4,
            color = Habit.Data.ColorType.COLOR4,
            weekIndices = setOf(today.dayOfWeek.ordinal) // Только воскресенье
        ).also { habit ->
            habitRepository.create(habit)
            // Запрашиваем субботу
            getHabitFactsForDate(yesterday, today, habitFactRepository, habitRepository).size shouldBe 0
        }
    }

    test("should not create a new fact if requesting a past date without existing records") {
        val habitFactRepository = HabitFactRepositoryImpl()
        val habitRepository = HabitRepositoryImpl()

        Habit.create(
            name = "Water",
            icon = Habit.Data.IconType.ICON5,
            color = Habit.Data.ColorType.COLOR5,
            weekIndices = setOf(yesterday.dayOfWeek.ordinal) // Только суббота
        ).also { habit ->
            habitRepository.create(habit)

            getHabitFactsForDate(yesterday, today, habitFactRepository, habitRepository).size shouldBe 0
            habitFactRepository.getHabitFacts(yesterday).size shouldBe 0
        }
    }
})
