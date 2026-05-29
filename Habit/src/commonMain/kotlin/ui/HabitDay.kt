package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitloop.app.habit.data.HabitRepositoryImpl
import com.habitloop.app.habit.data.HabitStorage
import com.habitloop.app.habit.domain.Habit
import com.habitloop.app.habit.domain.HabitRepository
import com.habitloop.app.habit.ui.model.HabitItem
import com.habitloop.app.habit.ui.model.toAbbr
import com.habitloop.app.habit.ui.model.toHabitItem
import com.habitloop.app.habit.ui.model.toRu
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HabitDayScreen(
    onAddHabitClick: () -> Unit = {},
    onHabitClick: (Int) -> Unit = {},
    onTabClick: (AppScreen) -> Unit = {}
) {
    // Палитра цветов по макету Figma
    val bgLightBlue = Color(0xFFF4F7FA)
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandBlue = Color(0xFF3B638A)

    val repository: HabitRepository = remember { HabitRepositoryImpl(HabitStorage()) }

    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    var selectedDate by remember { mutableStateOf(today) }
    var stateUpdateTrigger by remember { mutableStateOf(0) }

    val headerDateString = "${today.dayOfWeek.toRu()}, ${today.dayOfMonth} ${today.month.toRu()}"
    // горизонтальная плашка недели
    val weekDays = remember(today) {
        val currentDayOfWeekOrdinal = today.dayOfWeek.ordinal // 0 для Пн, 6 для Вс
        val mondayOfCurrentWeek = today.minus(currentDayOfWeekOrdinal, DateTimeUnit.DAY)

        List(7) { i -> List(7) { i -> mondayOfCurrentWeek.plus(i, DateTimeUnit.DAY) }
            val day = mondayOfCurrentWeek.plus(i, DateTimeUnit.DAY)
            Pair(day.dayOfMonth.toString(), day.dayOfWeek.toAbbr())
        }
    }

    // Инициализируем стейт.
    val habitsList = remember(selectedDate, stateUpdateTrigger) {
        val domainHabits = repository.getHabits()

        domainHabits
            .filter { it.createdDate <= selectedDate }
            .map { habit ->
                val isCompleted = repository.isHabitCompleted(habit.id, selectedDate)
                habit.toHabitItem(isCompleted = isCompleted)
            }
    }

    Scaffold(
        containerColor = bgLightBlue,
        // Правая нижняя плавающая кнопка "+"
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabitClick,
                containerColor = brandBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp).padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить", modifier = Modifier.size(28.dp))
            }
        },
        // Нижнее меню навигации (Bottom Navigation Bar)
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { onTabClick(AppScreen.HABIT_DAY) },
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    label = { Text("СЕГОДНЯ", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = brandBlue,
                        selectedTextColor = brandBlue,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onTabClick(AppScreen.STATISTICS) },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("СТАТИСТИКА", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onTabClick(AppScreen.SETTINGS) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("НАСТРОЙКИ", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Верхний блок: Профиль пользователя и Дата
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Алина, доброе утро!", fontSize = 14.sp, color = textGray)
                    Text(text = headerDateString, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                }
                // Имитация круглой аватарки
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFDCDCE2), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = textGray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Горизонтальный календарь на неделю
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEach { (date, dayName) ->
                    val isSelected = date == today.dayOfMonth.toString()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = dayName, fontSize = 12.sp, color = textGray)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(
                                    color = if (isSelected) brandBlue else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else textDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Список привычек дня
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(habitsList) { habit ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intId = habit.id
                                repository.toggleHabitCompletion(Habit.Id(intId), selectedDate)
                                ++stateUpdateTrigger
                            }
                    ) {
                        HabitCard(habit = habit)
                    }
                }
            }
        }
    }
}

// Компонент карточки привычки
@Composable
fun HabitCard(habit: HabitItem) {
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandBlue = Color(0xFF3B638A)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Иконка категории привычки
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(habit.iconBgColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = habit.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Текстовое описание привычки
                Column {
                    Text(
                        text = habit.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = habit.subtitle,
                        fontSize = 13.sp,
                        color = textGray
                    )
                }
            }

            // Чекбокс отметки (Круглая кнопка выполнения)
            IconButton(
                onClick = { /* Изменение стейта выполнения привычки */ },
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (habit.isCompleted) Color(0xFFE8F0FE) else Color(0xFFF4F4F6),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (habit.isCompleted) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = null,
                    tint = if (habit.isCompleted) brandBlue else textGray.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun HabitDayScreenPreview() {
    HabitDayScreen()
}
