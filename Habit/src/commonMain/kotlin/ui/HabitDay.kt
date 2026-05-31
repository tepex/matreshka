package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitloop.app.habit.domain.HabitFactRepository
import com.habitloop.app.habit.domain.HabitRepository
import com.habitloop.app.habit.domain.getHabitFactsForDate
import com.habitloop.app.habit.domain.model.HabitFactAggregate
import com.habitloop.app.habit.ui.model.toRu
import kotlinx.datetime.*

@Composable
fun HabitDayScreen(
    habitRepository: HabitRepository,
    habitFactRepository: HabitFactRepository,
    onAddHabitClick: () -> Unit = {},
    onHabitClick: (Int) -> Unit = {},
    onTabClick: (AppScreen) -> Unit = {}
) {
    // Палитра цветов по макету Figma
    val bgLightBlue = Color(0xFFF4F7FA)
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandBlue = Color(0xFF3B638A)

    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    var selectedDate by remember { mutableStateOf(today) }
    var stateUpdateTrigger by remember { mutableStateOf(0) }

    // Текст заголовка (например, "Четверг, 2 апреля") сформирован с помощью ваших новых мапперов
    val headerDateString = "${selectedDate.dayOfWeek.toRu()}, ${selectedDate.dayOfMonth} ${selectedDate.month.toRu()}"

    // Получение списка привычек через новую доменную функцию
    val habitsList = remember(selectedDate, stateUpdateTrigger) {
        getHabitFactsForDate(
            date = selectedDate,
            today = today,
            habitFactRepository = habitFactRepository,
            habitRepository = habitRepository
        )
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

            // 2. Горизонтальный календарь на неделю (Вынесенный вами компонент)
            HabitWeekCalendar(
                selectedDate = selectedDate,
                today = today,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Список привычек дня на базе HabitFactAggregate
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(habitsList) { aggregate ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: Реализовать логику клика/переключения статуса через Use Case.
                                // Будет сделано на следующем шаге после анализа полей доменной модели.
                                ++stateUpdateTrigger
                            }
                    ) {
                        HabitCard(aggregate = aggregate)
                    }
                }
            }
        }
    }
}

// Компонент карточки привычки, адаптированный под HabitFactAggregate и макет Figma
@Composable
fun HabitCard(aggregate: HabitFactAggregate) {
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandBlue = Color(0xFF3B638A)
    val borderGray = Color(0xFFE5E5EA)

    // Временные заглушки для полей.
    // Замените на реальные свойства из ваших моделей Habit/HabitFact на следующем шаге.
    val habitName = "Привычка" // Пример: aggregate.habit.name
    val isCompleted = false   // Пример: aggregate.fact.isCompleted
    val streakText = "🔥 0 дней" // Пример: "🔥 ${aggregate.streak} дней"

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
                modifier = Modifier.weight(1f)
            ) {
                // Круглая цветная иконка привычки слева по макету
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(brandBlue.copy(alpha = 0.12f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = null,
                        tint = brandBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Текстовая информация
                Column {
                    Text(
                        text = habitName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = streakText,
                        fontSize = 12.sp,
                        color = textGray
                    )
                }
            }

            // Круглый чекбокс состояния выполнения справа по макету
            Icon(
                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Статус",
                tint = if (isCompleted) brandBlue else borderGray,modifier = Modifier.size(28.dp)
            )
        }
    }
}
