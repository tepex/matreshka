package com.habitloop.app.habit.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.ui.graphics.Color
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFactAggregate
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

fun Habit.Data.IconType.toIcon() = when (this) {
    Habit.Data.IconType.ICON1 -> Icons.AutoMirrored.Filled.DirectionsRun
    Habit.Data.IconType.ICON2 -> Icons.AutoMirrored.Filled.MenuBook
    Habit.Data.IconType.ICON3 -> Icons.Default.SelfImprovement
    Habit.Data.IconType.ICON4 -> Icons.Default.LocalDrink
    Habit.Data.IconType.ICON5 -> Icons.Default.SportsTennis
    Habit.Data.IconType.ICON6 -> Icons.Default.SportsBasketball
    Habit.Data.IconType.ICON7 -> Icons.Default.SportsSoccer
}

fun Habit.Data.ColorType.toColor() = when (this) {
    Habit.Data.ColorType.COLOR1 -> Color(0xFF0066CC)
    Habit.Data.ColorType.COLOR2 -> Color(0xFFE53935)
    Habit.Data.ColorType.COLOR3 -> Color(0xFFFFB300)
    Habit.Data.ColorType.COLOR4 -> Color(0xFF4CAF50)
    Habit.Data.ColorType.COLOR5 -> Color(0xFF1C1C1E)
    Habit.Data.ColorType.COLOR6 -> Color(0xFF9C27B0)
    Habit.Data.ColorType.COLOR7 -> Color(0xFFC7A167)
}
fun HabitFactAggregate.toHabitItem(): HabitItem =
    HabitItem(
        id = fact.habitId,
        name = habitData.name.value,
        icon = habitData.icon.toIcon(),
        iconBgColor = habitData.color.toColor(),
        isCompleted = fact.isCompleted
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

/**
 * Функция для правильного склонения слова "день" в зависимости от числа серий подряд
 */
fun getDaysWord(streak: Int): String =
    if ((streak % 100) in 11..19) "дней" else when (streak % 10) {
        1 -> "день"
        2, 3, 4 -> "дня"
        else -> "дней"
    }
