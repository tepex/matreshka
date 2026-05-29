package com.habitloop.app.habit.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.ui.graphics.Color
import com.habitloop.app.habit.domain.Habit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

fun Habit.Type.toIcon() = when (this) {
    Habit.Type.RUN -> Icons.AutoMirrored.Filled.DirectionsRun
    Habit.Type.READ -> Icons.AutoMirrored.Filled.MenuBook
    Habit.Type.MEDITATE -> Icons.Default.SelfImprovement
    Habit.Type.DRINK -> Icons.Default.LocalDrink
    Habit.Type.TENNIS -> Icons.Default.SportsTennis
    Habit.Type.BASKETBALL -> Icons.Default.SportsBasketball
    Habit.Type.SOCCER -> Icons.Default.SportsSoccer
}

fun Habit.TypeColor.toColor() = when (this) {
    Habit.TypeColor.COLOR1 -> Color(0xFF0066CC)
    Habit.TypeColor.COLOR2 -> Color(0xFFE53935)
    Habit.TypeColor.COLOR3 -> Color(0xFFFFB300)
    Habit.TypeColor.COLOR4 -> Color(0xFF4CAF50)
    Habit.TypeColor.COLOR5 -> Color(0xFF1C1C1E)
    Habit.TypeColor.COLOR6 -> Color(0xFF9C27B0)
    Habit.TypeColor.COLOR7 -> Color(0xFFC7A167)
}
fun Habit.toHabitItem(isCompleted: Boolean): HabitItem =
    HabitItem(
        id = this.id.value,
        title = this.name.value,
        subtitle = this.description.value,
        icon = type.toIcon(),
        iconBgColor = typeColor.toColor(),
        isCompleted = isCompleted
    )

fun DayOfWeek.toAbbr(): String =
    listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")[ordinal]

fun Month.toRu(): String =
    listOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )[ordinal]

fun DayOfWeek.toRu(): String =
    listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")[ordinal]
