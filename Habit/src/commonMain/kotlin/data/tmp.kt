package com.habitloop.app.habit.data

import com.habitloop.app.habit.domain.model.Habit
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
val defaultHabits = listOf(
    Habit(Habit.Id(1), Habit.Name("Утренняя пробежка"), Habit.Description("11 дней"), Habit.Type.RUN, Habit.TypeColor.COLOR1, today, false),
    Habit(Habit.Id(2), Habit.Name("Чтение 30 мин"), Habit.Description("5 дней"), Habit.Type.READ, Habit.TypeColor.COLOR2, today, false),
    Habit(Habit.Id(3), Habit.Name("Медитация"), Habit.Description("31 день подряд"), Habit.Type.MEDITATE, Habit.TypeColor.COLOR3, today, false),
    Habit(Habit.Id(4), Habit.Name("Пить воду 2л"), Habit.Description("0 дней"), Habit.Type.DRINK, Habit.TypeColor.COLOR4, today, false)
)
