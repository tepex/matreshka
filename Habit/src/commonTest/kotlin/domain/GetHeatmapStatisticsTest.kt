package com.habitloop.app.habit.domain

import com.habitloop.app.habit.data.HabitFactRepositoryImpl
import com.habitloop.app.habit.data.HabitRepositoryImpl
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import com.habitloop.app.habit.domain.model.HeatmapPeriod
import com.habitloop.app.habit.domain.model.HeatmapState
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate

class GetHeatmapStatisticsTest : FunSpec({

    lateinit var habitRepository: HabitRepository
    lateinit var habitFactRepository: HabitFactRepository

    // 10 апреля 2026 года — это Пятница.
    // Дни текущей недели: Пн(6), Вт(7), Ср(8), Чт(9), Пт(10 - сегодня), Сб(11 - будущее), Вс(12 - будущее)
    val today = LocalDate(2026, 4, 10)

    beforeTest {
        habitRepository = HabitRepositoryImpl()
        habitFactRepository = HabitFactRepositoryImpl()
    }

    context("Heatmap matrix generation based on statistic.adoc specification") {
        test("should return correct row count depending on chosen HeatmapPeriod") {
            Habit.create(
                name = "Daily Gym",
                icon = Habit.Data.IconType.entries.first(),
                color = Habit.Data.ColorType.entries.first(),
                weekIndices = setOf(0, 1, 2, 3, 4, 5, 6)
            ).also { habit ->
                habitRepository.create(habit)

                // 7 дней -> 1 ряд (строка)
                getHeatmapStatistics(habit, habitFactRepository, today, HeatmapPeriod.WEEK1).run {
                    rows.size shouldBe 1
                    rows[0].cells.size shouldBe 7
                }

                // 28 дней -> 4 ряда (строки)
                getHeatmapStatistics(habit, habitFactRepository, today, HeatmapPeriod.WEEK4).run {
                    rows.size shouldBe 4
                    rows.forEach { row -> row.cells.size shouldBe 7 }
                }
            }
        }

        test("should fill future days of current week and unscheduled days with EMPTY state") {
            // Создаем привычку только по Вторникам (индекс 1) и Пятницам (индекс 4)
            Habit.create(
                name = "Smart Water",
                icon = Habit.Data.IconType.entries.first(),
                color = Habit.Data.ColorType.entries.first(),
                weekIndices = setOf(1, 4)
            ).also { habit ->
                habitRepository.create(habit)

                getHeatmapStatistics(habit, habitFactRepository, today, HeatmapPeriod.WEEK1).run {
                    val currentWeekCells = rows[0].cells
                    // Понедельник (индекс 0) — не в расписании -> EMPTY
                    currentWeekCells[0] shouldBe HeatmapState.EMPTY
                    // Вторник (индекс 1) — в расписании, прошлый день -> по дефолту NOT_COMPLETED
                    currentWeekCells[1] shouldBe HeatmapState.NOT_COMPLETED
                    // Суббота и Воскресенье (индексы 5 и 6) — будущие дни текущей недели -> EMPTY
                    currentWeekCells[5] shouldBe HeatmapState.EMPTY
                    currentWeekCells[6] shouldBe HeatmapState.EMPTY
                }
            }
        }

        test("should correctly identify COMPLETED and NOT_COMPLETED days from repository history") {
            Habit.create(
                name = "Read Books",
                icon = Habit.Data.IconType.entries.first(),
                color = Habit.Data.ColorType.entries.first(),
                weekIndices = setOf(0, 1, 2, 3, 4, 5, 6)
            ).also { habit ->
                habitRepository.create(habit)

                // Добавляем факт выполнения на вчера (Четверг, 9 апреля)
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 9),
                    listOf(HabitFact.create(habit.id).setCompletion(true))
                )
                // Добавляем факт пропуска на позавчера (Среда, 8 апреля)
                habitFactRepository.addAll(
                    LocalDate(2026, 4, 8),
                    listOf(HabitFact.create(habit.id).setCompletion(false))
                )

                getHeatmapStatistics(habit, habitFactRepository, today, HeatmapPeriod.WEEK1).run {
                    rows[0].cells.also { currentWeekCells ->
                        // Среда (индекс 2) -> NOT_COMPLETED (явно записан как false)
                        currentWeekCells[2] shouldBe HeatmapState.NOT_COMPLETED
                        // Четверг (индекс 3) -> COMPLETED (записан как true)
                        currentWeekCells[3] shouldBe HeatmapState.COMPLETED
                        // Пятница (индекс 4 - сегодня) -> данных в репозитории нет, значит NOT_COMPLETED
                        currentWeekCells[4] shouldBe HeatmapState.NOT_COMPLETED
                    }
                }
            }
        }
    }
})
