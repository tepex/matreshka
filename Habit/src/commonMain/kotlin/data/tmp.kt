package com.habitloop.app.habit.data

import com.habitloop.app.habit.domain.Habit

val defaultHabits = listOf(
    Habit(1, "Утренняя пробежка", "11 дней", Habit.Type.RUN, true),
    Habit(2, "Чтение 30 мин", "5 дней", Habit.Type.READ, true),
    Habit(3, "Медитация", "31 день подряд - рекорд!", Habit.Type.MEDITATE, false),
    Habit(4, "Пить воду 2л", "0 дней", Habit.Type.DRINK, false)
)
