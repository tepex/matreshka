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
                        takeIf { date == today }?.let { origin ->
                            origin.takeIf { it.size < habits.size }?.let {
                                origin.mapTo(HashSet(origin.size)) { it.fact.habitId }
                                    .let { processedIds ->
                                        buildList(habits.size) {
                                            addAll(origin)
                                            for (habit in habits) {
                                                if (habit.id !in processedIds) add(HabitFactAggregate.create(habit))
                                            }
                                        }
                                    }
                            } ?: origin
                        } ?: this
                    }
            } ?: habits.map { HabitFactAggregate.create(habit = it) }
        }
    }
