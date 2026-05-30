package com.habitloop.app.habit.domain

import com.habitloop.app.habit.domain.model.DayHabitFacts
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import com.habitloop.app.habit.domain.model.HabitFactAggregate
import kotlinx.datetime.LocalDate

/** Актуальные и активные [HabitFact] для заданного дня.
 *
 * * Актуальные — проверяется наличие [Habit]. Если такого уже нет, то [HabitFact] удаляется.
 * Если для дня недели заданного дня есть [Habit], но нет соответствующего [HabitFact] для текущего дня, то он создается.
 * * Активные — выбираются только те, у которых [Habit] не заморожен.
 *
 * @param date текущий день
 * @param habitFactRepository репозиторий [HabitFact]
 * @param habitRepository репозиторий [Habit]
 * @return [DayHabitFacts] для текущего дня
 * */
fun `get habit facts for date`(
    date: LocalDate,
    today: LocalDate,
    habitFactRepository: HabitFactRepository,
    habitRepository: HabitRepository
): List<HabitFactAggregate> =
    habitRepository.getHabitsByDayOfWeek(date.dayOfWeek).associate { it.id to it.data }.let { habitMap ->
        habitFactRepository.takeIf { date <= today }?.let { repo ->
            repo.getHabitFacts(date)
                .mapNotNull { fact -> habitMap[fact.habitId]?.let { HabitFactAggregate.create(fact, it) } }
                .run {
                    takeIf { date == today }?.`extend today habit facts with new habits`(today, habitRepository) ?: this
                }
        } ?: habitMap.map { (id, data) -> HabitFactAggregate.create(HabitFact.create(id), data) }
    }

fun List<HabitFactAggregate>.`extend today habit facts with new habits`(
    today: LocalDate,
    habitRepository: HabitRepository
): List<HabitFactAggregate> =
    habitRepository.getHabitsByDayOfWeek(today.dayOfWeek).let { habits ->
        buildList(habits.size) {
            addAll(this@`extend today habit facts with new habits`)
            for (habit in habits) {
                if (!any { it.fact.habitId == habit.id })
                    add(HabitFactAggregate.create(HabitFact.create(habit.id), habit.data))
            }
        }
    }
