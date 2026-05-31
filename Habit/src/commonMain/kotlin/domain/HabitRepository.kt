package com.habitloop.app.habit.domain

import com.habitloop.app.habit.domain.model.Habit
import kotlinx.datetime.DayOfWeek

interface HabitRepository {

    //fun getHabits(): Set<Habit>

    fun getHabitsByDayOfWeek(day: DayOfWeek): Set<Habit>

    /*
    fun getHabit(id: Habit.Id): Result<Habit>

    fun create(data: Habit.Data): Result<Habit>

    fun update(id: Habit.Id, data: Habit.Data): Result<Habit>

    fun delete(id: Habit.Id): Result<Habit>*/
}
