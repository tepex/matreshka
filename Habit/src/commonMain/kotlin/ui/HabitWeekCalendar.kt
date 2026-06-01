package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitloop.app.habit.ui.model.toAbbr
import kotlinx.datetime.*

@Composable
fun HabitWeekCalendar(
    selectedDate: LocalDate,
    today: LocalDate = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date },
    onDateSelected: (LocalDate) -> Unit
) {
    // Палитра цветов строго по Figma макету
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandBlue = Color(0xFF3B638A)
    val borderGray = Color(0xFFE5E5EA) // Светлая подложка-ободок для неактивных дней

    // Вычисляем список дней для текущей недели на основе выбранной даты
    val weekDays = remember(today) {
        List(7) { i -> today.plus(i - 3, DateTimeUnit.DAY) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDays.forEach { date ->
            val isSelected = date == selectedDate
            val isSameMonth = date.month == selectedDate.month

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onDateSelected(date) }
            ) {
                // 1. Аббревиатура дня недели (Пн, Вт, Ср...)
                Text(
                    text = date.dayOfWeek.toAbbr(),
                    fontSize = 12.sp,
                    color = textGray,
                    fontWeight = FontWeight.Medium
                )

                // 2. Круглая плашка с числом месяца
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = if (isSelected) brandBlue else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = if (!isSelected && !isSameMonth) 1.5.dp else 0.dp,
                            color = if (!isSelected && !isSameMonth) borderGray else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else textDark
                    )
                }
            }
        }
    }
}
