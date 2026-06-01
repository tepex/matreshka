package com.habitloop.app.habit

import com.habitloop.app.habit.data.HabitFactRepositoryImpl
import com.habitloop.app.habit.data.HabitRepositoryImpl
import com.habitloop.app.habit.domain.HabitFactRepository
import com.habitloop.app.habit.domain.HabitRepository

class DI {
    val habitRepository: HabitRepository by lazy { HabitRepositoryImpl() }
    val habitFactRepository: HabitFactRepository by lazy { HabitFactRepositoryImpl() }
}
