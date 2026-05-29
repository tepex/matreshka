package com.habitloop.app.habit.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.ui.graphics.Color
import com.habitloop.app.habit.domain.Habit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

fun Habit.toHabitItem(): HabitItem {
    val (icon, color) = when (type) {
        Habit.Type.RUN -> Pair(Icons.AutoMirrored.Filled.DirectionsRun, Color(0xFFE57373))
        Habit.Type.READ -> Pair(Icons.Default.Book, Color(0xFF64B5F6))
        Habit.Type.MEDITATE -> Pair(Icons.Default.SelfImprovement, Color(0xFF81C784))
        Habit.Type.DRINK -> Pair(Icons.Default.LocalDrink, Color(0xFFFFB74D))
        Habit.Type.EXERCISE -> Pair(Icons.Default.FitnessCenter, Color(0xFFBA68C8))
        Habit.Type.OTHER -> Pair(Icons.AutoMirrored.Filled.HelpOutline, Color(0xFF90A4AE))
    }

    return HabitItem(
        id = id,
        title = name,
        subtitle = description,
        icon = icon,
        iconBgColor = color,
        isCompleted = isCompleted
    )
}

fun DayOfWeek.toAbbr(): String =
    listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")[ordinal]

fun Month.toRu(): String =
    listOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )[ordinal]

fun DayOfWeek.toRu(): String =
    listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")[ordinal]
