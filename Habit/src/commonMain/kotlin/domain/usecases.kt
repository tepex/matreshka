package com.habitloop.app.habit.domain

import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import com.habitloop.app.habit.domain.model.HabitFactAggregate
import com.habitloop.app.habit.domain.model.HabitStatistics
import com.habitloop.app.habit.domain.model.HeatmapPeriod
import com.habitloop.app.habit.domain.model.HeatmapState
import com.habitloop.app.habit.ui.model.HeatmapRow
import com.habitloop.app.habit.ui.model.HeatmapUiState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/** Актуальные и активные [HabitFact] для заданного дня.
 *
 * * Актуальные — проверяется наличие [Habit]. Если такого уже нет, то [HabitFact] удаляется.
 * Если для дня недели заданного дня есть [Habit], но нет соответствующего [HabitFact] для текущего дня, то он создается.
 * * Активные — выбираются только те, у которых [Habit] не заморожен.
 *
 * @param date заданный день
 * @param today текущий день
 * @param habitFactRepository репозиторий [HabitFact]
 * @param habitRepository репозиторий [Habit]
 * @return [List]<[HabitFactAggregate]> для текущего дня
 * */
fun getHabitFactsForDate(
    date: LocalDate,
    today: LocalDate,
    habitFactRepository: HabitFactRepository,
    habitRepository: HabitRepository
): List<HabitFactAggregate> =
    habitRepository.getHabitsByDayOfWeek(date.dayOfWeek).let { habits ->
        habits.associate { it.id to it.data }.let { habitMap ->
            habitFactRepository.takeIf { date <= today }?.let { repo ->
                repo.getHabitFacts(date)
                    .mapNotNull { fact -> habitMap[fact.habitId]?.let { HabitFactAggregate.create(fact, it) } }
                    .run {
                        takeIf { date == today }?.let { originFacts ->
                            originFacts.takeIf { it.size < habits.size }?.let {
                                println("[usecase]: adding new facts for new habits")
                                originFacts.mapTo(HashSet(originFacts.size)) { it.fact.habitId }
                                    .let { processedIds ->
                                        buildList(habits.size) {
                                            addAll(originFacts)
                                            for (habit in habits) {
                                                if (habit.id !in processedIds) {
                                                    HabitFactAggregate.create(habit).also {
                                                        habitFactRepository.add(today, it.fact)
                                                        add(it)
                                                        println("[usecase]: added new habit fact: ${it.fact}")
                                                    }
                                                }
                                            }
                                        }
                                    }
                            } ?: originFacts
                        } ?: this
                    }
            } ?: habits.map { HabitFactAggregate.create(habit = it) }
        }
    }.also {
        println("[usecase] facts for $date: ${habitFactRepository.getHabitFacts(date)}")
    }

fun switchHabitFactForDate(
    date: LocalDate,
    i: Int,
    habitFactRepository: HabitFactRepository
) {
    val newState = habitFactRepository.getHabitFacts(date)[i].switchCompletion()
    habitFactRepository.update(date, i, newState)

}

/**
 * Use Case (Интерактор) для создания и сохранения новой привычки
 */
fun createNewHabit(
    habit: Habit,
    habitRepository: HabitRepository
) {
    // Здесь при необходимости может быть дополнительная бизнес-логика
    // (например, проверка на дубликаты имен, валидация лимитов привычек и т.д.)

    habitRepository.create(habit)
}

/**
 * @see docs/statistics.adoc
 * */
fun getHabitStatistics(
    habit: Habit,
    habitFactRepository: HabitFactRepository,
    today: LocalDate
): HabitStatistics {
    val startDate = habitFactRepository.getStartDate(habit.id).getOrElse { today }
    // Формируем список всех дней от startDate до вчерашнего дня включительно
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    val activeDaysHistory = mutableListOf<LocalDate>()
    var currentIterationDate = startDate

    while (currentIterationDate <= yesterday) {
        // Учитываем только те дни, которые активны по расписанию этой конкретной привычки
        if (habit.data.weekly.value[currentIterationDate.dayOfWeek.ordinal])
            activeDaysHistory.add(currentIterationDate)
        currentIterationDate = currentIterationDate.plus(1, DateTimeUnit.DAY)
    }

    // Проверяем, выполнена ли привычка сегодня (если день активный по расписанию)
    val isCompletedToday = habit.data.weekly.value[today.dayOfWeek.ordinal] &&
            habitFactRepository.getHabitFacts(today).find { it.habitId.value == habit.id.value }?.isCompleted == true

    // вычисление серий (streaks)
    var bestStreak = 0
    var runningStreak = 0

    // Идем в хронологическом порядке для вычисления лучшей серии
    for (date in activeDaysHistory) {
        val isCompleted = habitFactRepository.getHabitFacts(date)
            .find { it.habitId.value == habit.id.value }?.isCompleted == true
        if (isCompleted) {
            ++runningStreak
            if (runningStreak > bestStreak) bestStreak = runningStreak
        } else runningStreak = 0 // сброс серии при пропуске
    }

    // Вычисляем текущую серию (идем с конца истории в прошлое)
    // Если сегодня еще не выполнено, проверяем, не прервалась ли серия вчера
    val currentStreak = if (isCompletedToday) runningStreak + 1 else runningStreak
    // Корректируем лучшую серию, если текущая (с учетом сегодняшнего выполнения) оказалась длиннее
    if (currentStreak > bestStreak) bestStreak = currentStreak

    // вычисление процента выполнения за 30 дней (success rate)
    var totalFactsIn30Days = 0
    var completedFactsIn30Days = 0
    var checkDate = today.minus(30, DateTimeUnit.DAY)

    while (checkDate <= today) {
        habitFactRepository.getHabitFacts(checkDate).find { it.habitId.value == habit.id.value }
            ?.also { targetFact ->
                // Согласно спецификации, считаем дни, для которых физически существует запись факта
                ++totalFactsIn30Days
                if (targetFact.isCompleted) ++completedFactsIn30Days
            }
        checkDate = checkDate.plus(1, DateTimeUnit.DAY)
    }

    return HabitStatistics(
        currentStreak,
        bestStreak,
        if (totalFactsIn30Days > 0) ((completedFactsIn30Days.toDouble() / totalFactsIn30Days) * 100).toInt() else 0
    ).also { println("[stat] $it") }
}

fun getHeatmapStatistics(
    habit: Habit,
    habitFactRepository: HabitFactRepository,
    today: LocalDate,
    period: HeatmapPeriod
): HeatmapUiState {
    // понедельник текущей календарной недели
    // самый первый понедельник начала сетки на основе period.count (количество недель)
    val startMonday = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
        .minus((period.rowsCount - 1) * 7, DateTimeUnit.DAY)
    val completedDates = habitFactRepository.getCompletedDatesByHabitId(habit.id)

    // список всех дат для отображения сетки
    return List(period.rowsCount * 7) { i -> startMonday.plus(i, DateTimeUnit.DAY) }
        // Маппим даты в HeatmapState, используя habit.data.weekly.value (BooleanArray)
        .map { date ->
            // Преобразуем isoDayNumber (1..7) в индекс массива (0..6)
            val isScheduled = habit.data.weekly.value[date.dayOfWeek.isoDayNumber - 1]
            when {
                !isScheduled || (date > today) -> HeatmapState.EMPTY
                completedDates.contains(date) -> HeatmapState.COMPLETED
                else -> HeatmapState.NOT_COMPLETED
            }
        }.chunked(7).map { HeatmapRow(it) }.let(::HeatmapUiState)
}
