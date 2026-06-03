package com.habitloop.app.habit

import com.habitloop.app.habit.data.HabitFactRepositoryImpl
import com.habitloop.app.habit.data.HabitRepositoryImpl
import com.habitloop.app.habit.domain.HabitFactRepository
import com.habitloop.app.habit.domain.HabitRepository
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

class DI {
    val habitRepository: HabitRepository by lazy { HabitRepositoryImpl() }
    val habitFactRepository: HabitFactRepository by lazy { HabitFactRepositoryImpl() }

    init {
        generateTestData(
            habitRepository = habitRepository,
            habitFactRepository = habitFactRepository
        )
    }

    /**
     * Алгоритм генерации тестовых данных:
     * 1. Создает случайное количество привычек (от [minHabits] до [maxHabits]) с именами "Fake habit $n". Для каждой
     *    привычки случайно выбираются иконка, цвет и расписание активных дней недели (минимум 1 день должен быть
     *    активен). Созданные привычки сохраняются в репозиторий.
     * 2. Вычисляет полный диапазон дат с [startDate] по вчерашний день включительно.
     * 3. Формирует множество случайных съёмочных дней из этого периода. Количество выбранных дат регулируется
     *    параметром [daysDensity] (например, 70% от общего числа дней в периоде).
     * 4. Для каждого выбранного дня определяет список привычек, которые по своему расписанию могут выполняться в этот
     *    день недели.
     * 5. Из доступных для этого дня привычек случайным образом выбирает от 1 до [maxHabits] уникальных [Habit]
     *    (если доступных привычек меньше [maxHabits], берутся все доступные).
     * 6. Для каждой выбранной привычки создается [HabitFact] и сохраняется в репозиторий.
     *
     * @param minHabits минимальное кол-во привычек
     * @param maxHabits максимальное кол-во привычек
     * @param daysDensity Плотность дней (по умолчанию 70%)
     * @param startDate начальная дата генерации
     * @param habitRepository репозиторий привычек
     * @param habitFactRepository репозиторий фактов
     */
    fun generateTestData(
        minHabits: Int = 5,
        maxHabits: Int = 12,
        daysDensity: Double = 0.70,
        startDate: LocalDate = LocalDate.parse("2026-01-01"),
        habitRepository: HabitRepository,
        habitFactRepository: HabitFactRepository,
    ) {
        // генерация привычек
        val totalHabitsCount = Random.nextInt(minHabits, maxHabits + 1)
        val createdHabits = mutableListOf<Habit>()

        for (n in 1..totalHabitsCount) {
            // Генерируем случайное расписание (Set с индексами дней от 0 до 6)
            // Гарантируем, что выбран хотя бы один день недели, чтобы избежать пустых привычек
            val availableIndices = (0..6).shuffled()
            val activeDaysCount = Random.nextInt(1, 8) // От 1 до 7 активных дней

            Habit.create(
                name = "Fake habit $n",
                icon = Habit.Data.IconType.entries.random(),
                color = Habit.Data.ColorType.entries.random(),
                weekIndices = availableIndices.take(activeDaysCount).toSet()
            ).also { habit ->
                habitRepository.create(habit)
                createdHabits.add(habit)
            }
        }

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        // Собираем все дни периода в один плоский список
        val allDaysInPeriod = mutableListOf<LocalDate>()
        var currentIterationDate = startDate
        while (currentIterationDate < today) {
            allDaysInPeriod.add(currentIterationDate)
            currentIterationDate = currentIterationDate.plus(1, DateTimeUnit.DAY)
        }

        // отбор случайных дней по плотности daysDensity
        val targetDaysCount = (allDaysInPeriod.size * daysDensity).toInt().coerceAtLeast(1)
        val selectedDates = allDaysInPeriod.shuffled().take(targetDaysCount)
        println("[DI] targetDaysCount: $targetDaysCount/${allDaysInPeriod.size}")
        println("[DI] selectedDays: $selectedDates (${selectedDates.size})")

        // генерация фактов выполнения с учетом расписания привычек
        for (date in selectedDates) {
            // Фильтруем созданные привычки: берем только те, которые активны в этот день недели
            createdHabits.filter { it.data.weekly.value[date.dayOfWeek.ordinal] }.also { habitsActiveToday ->
                // Если в этот день недели по расписанию вообще нет активных привычек, пропускаем день
                if (habitsActiveToday.isEmpty()) continue

                // Берем случайные уникальные привычки из доступных на сегодня
                habitsActiveToday.shuffled().take(
                    // Вычисляем, сколько уникальных фактов сгенерировать в этот день (от 1 до maxHabits)
                    // Ограничиваем сверху количеством реально доступных сегодня привычек
                    Random.nextInt(1, habitsActiveToday.size.coerceAtMost(maxHabits) + 1)
                ).also { selectedHabitsForDay ->
                    // Создаем и сохраняем факты выполнения
                    mutableListOf<HabitFact>().apply {
                        for (habit in selectedHabitsForDay) add(HabitFact.create(habit.id).setCompletion(true))
                    }.also { habitFactRepository.addAll(date, it) }
                }
            }
        }
    }
}
