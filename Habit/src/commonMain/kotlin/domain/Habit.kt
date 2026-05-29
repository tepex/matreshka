package com.habitloop.app.habit.domain

data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val type: Type,
    val isCompleted: Boolean
) {
    enum class Type {
        RUN, READ, MEDITATE, DRINK, EXERCISE, OTHER
    }
}

