package com.habitloop.app.habit.domain

import com.habitloop.app.habit.data.HabitFactRepositoryImpl
import com.habitloop.app.habit.data.HabitRepositoryImpl
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class GetHabitStatisticsTest : FunSpec({

    lateinit var habitRepository: HabitRepository
    lateinit var habitFactRepository: HabitFactRepository

    val today = LocalDate(2026, 4, 10) // Friday

    beforeTest {
        habitRepository = HabitRepositoryImpl()
        habitFactRepository = HabitFactRepositoryImpl()
    }

    context("Habit statistics calculation based on statistic.adoc specification") {

        test("should return all zeros when history is completely empty") {
            Habit.create(
                name = "Test Habit",
                icon = Habit.Data.IconType.entries.first(),
                color = Habit.Data.ColorType.entries.first(),
                weekIndices = setOf(0, 1, 2, 3, 4, 5, 6)
            ).also { habit ->
                habitRepository.create(habit)

                getHabitStatistics(habit.id, habitRepository, habitFactRepository, today).run {
                    currentStreak shouldBe 0
                    bestStreak shouldBe 0
                    successRate30Days shouldBe 0
                }
            }
        }

        test("should correctly accumulate continuous current streak") {
            Habit.create(
                "Test Habit",
                Habit.Data.IconType.entries.first(),
                Habit.Data.ColorType.entries.first(),
                setOf(0, 1, 2, 3, 4, 5, 6)
            ).also { habit ->
                habitRepository.create(habit)

                habitFactRepository.addAll(
                    LocalDate(2026, 4, 9),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 8),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 7),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )

                getHabitStatistics(habit.id, habitRepository, habitFactRepository, today).run {
                    currentStreak shouldBe 3
                    bestStreak shouldBe 3
                }
            }
        }

        test("should correctly calculate the best streak when gaps are present") {
            Habit.create(
                "Test Habit",
                Habit.Data.IconType.entries.first(),
                Habit.Data.ColorType.entries.first(),
                setOf(0, 1, 2, 3, 4, 5, 6)
            ).also { habit ->
                habitRepository.create(habit)

                // Streak 1: 2 completed days
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 1),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 2),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )

                // 2026-04-03 — Gap (streak resets)

                // Streak 2: 4 completed days (this should be the best streak)
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 4),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 5),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 6),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 7),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )

                getHabitStatistics(habit.id, habitRepository, habitFactRepository, today).run {
                    bestStreak shouldBe 4
                    currentStreak shouldBe 0
                }
            }
        }

        test("should calculate 30 days success rate based only on existing records") {
            Habit.create(
                "Test Habit",
                Habit.Data.IconType.entries.first(),
                Habit.Data.ColorType.entries.first(),
                setOf(0, 1, 2, 3, 4, 5, 6)
            ).also { habit ->
                habitRepository.create(habit)

                // Generate 10 records within last 30 days: 6 completed (true), 4 incomplete (false)
                for (i in 1..6) {
                    habitFactRepository.addAll(
                        today.minus(i, kotlinx.datetime.DateTimeUnit.DAY),
                        listOf(HabitFact.create(habit.id).setCompletion(true))
                    )
                }
                for (i in 7..10) {
                    habitFactRepository.addAll(
                        today.minus(i, kotlinx.datetime.DateTimeUnit.DAY),
                        listOf(HabitFact.create(habit.id).setCompletion(false))
                    )
                }

                // 6 completed out of 10 existing facts = 60%
                getHabitStatistics(habit.id, habitRepository, habitFactRepository, today).successRate30Days shouldBe 60
            }
        }
    }
})
