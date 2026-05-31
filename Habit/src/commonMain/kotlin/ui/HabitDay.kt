package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import kotlinx.datetime.*

@Composable
fun HabitDayScreen(
    habitRepository: HabitRepository,
    habitFactRepository: HabitFactRepository,
    onAddHabitClick: () -> Unit = {},
    onHabitClick: (Int) -> Unit = {},
    onTabClick: (AppScreen) -> Unit = {}
) {
    // Точная палитра цветов из Figma
    val bgLightBlue = Color(0xFFF4F7FA)
    val textDark = Color(0xFF2C2C2C)
    val textGray = Color(0xFF9AA0A6)
    val brandBlue = Color(0xFF3B638A)
    val borderGray = Color(0xE0E0E0FF)

    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    var selectedDate by remember { mutableStateOf(today) }
    var stateUpdateTrigger by remember { mutableStateOf(0) }

    // Форматирование заголовка: Четверг, 2 апреля
    val headerDateString = "${selectedDate.dayOfWeek.toRu()}, ${selectedDate.dayOfMonth} ${selectedDate.month.toRu()}"

    // Список дней текущей недели для горизонтальной панели
    val weekDays = remember(selectedDate) {
        val currentDayOfWeekOrdinal = selectedDate.dayOfWeek.ordinal
        val mondayOfCurrentWeek = selectedDate.minus(currentDayOfWeekOrdinal, DateTimeUnit.DAY)
        List(7) { i -> mondayOfCurrentWeek.plus(i, DateTimeUnit.DAY) }
    }

    // Запрос данных через доменный Use Case
    val habitsAggregateList = remember(selectedDate, stateUpdateTrigger) {
        getHabitFactsForDate(
            date = selectedDate,
            today = today,
            habitFactRepository = habitFactRepository,
            habitRepository = habitRepository
        )
    }

    Scaffold(
        containerColor = bgLightBlue,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabitClick,
                containerColor = brandBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(56.dp)
                    .padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить", modifier = Modifier.size(28.dp))
            }
        },
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
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 1. Верхний блок: Приветствие, Время и Аватар
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Алина, доброе утро!", fontSize = 13.sp, color = textGray)
                    Text(text = headerDateString, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textDark)
                }
                // Круглая аватарка из макета
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFFE2E8F0), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = textGray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Горизонтальный календарь (Week Bar) ровно по Figma
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEach { date ->
                    val isSelected = date == selectedDate
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.clickable { selectedDate = date }
                    ) {
                        Text(
                            text = date.dayOfWeek.toAbbr(),
                            fontSize = 12.sp,
                            color = textGray,
                            fontWeight = FontWeight.Medium
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    color = if (isSelected) brandBlue else Color.White,
                                    shape = CircleShape
                                )
                                .border(
                                    width = if (isSelected) 0.dp else 1.dp,
                                    color = if (isSelected) Color.Transparent else borderGray,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else textDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. Список привычек дня
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(habitsAggregateList) { aggregate ->
                    // Временный маппинг флага выполнения.
                    // Замените на реальное поле из вашего HabitFact / HabitFactAggregate
                    val isCompleted = false

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Тоггл состояния привычки через репозиторий/интерактор
                                ++stateUpdateTrigger
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Цветная круглая иконка привычки слева
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = brandBlue.copy(alpha = 0.15f), // В Figma цвет зависит от категории
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsRun, // Замените динамически на иконку привычки
                                    contentDescription = null,
                                    tint = brandBlue,modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))// Текстовый блок (Название + Серия дней)

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = aggregate.habit.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textDark
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                            }



                            // Отображение счетчика дней подряд (например, "🔥 5 дней")

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🔥 5 дней", // Замените на динамические данные из вашей модели aggregate/fact
                                    fontSize = 12.sp,
                                    color = textGray
                                )
                            }
                        }
                        // Круглый чекбокс справа ровно как на макете
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Статус выполнения",
                            tint = if (isCompleted) brandBlue else borderGray,modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
}

// Функции перевода дат остаются без изменений

fun DayOfWeek.toRu(): String = when(this) {DayOfWeek.MONDAY -> "Понедельник"DayOfWeek.TUESDAY -> "Вторник"DayOfWeek.WEDNESDAY -> "Среда"DayOfWeek.THURSDAY -> "Четверг"DayOfWeek.FRIDAY -> "Пятница"DayOfWeek.SATURDAY -> "Суббота"DayOfWeek.SUNDAY -> "Воскресенье"else -> this.name}fun DayOfWeek.toAbbr(): String = when(this) {DayOfWeek.MONDAY -> "Пн"DayOfWeek.TUESDAY -> "Вт"DayOfWeek.WEDNESDAY -> "Ср"DayOfWeek.THURSDAY -> "Чт"DayOfWeek.FRIDAY -> "Пт"DayOfWeek.SATURDAY -> "Сб"DayOfWeek.SUNDAY -> "Вс"else -> this.name}fun Month.toRu(): String = when(this) {Month.JANUARY -> "января"Month.FEBRUARY -> "февраля"Month.MARCH -> "марта"Month.APRIL -> "апреля"Month.MAY -> "мая"Month.JUNE -> "июня"Month.JULY -> "июля"Month.AUGUST -> "августа"Month.SEPTEMBER -> "сентября"Month.OCTOBER -> "октября"Month.NOVEMBER -> "ноября"Month.DECEMBER -> "декабря"else -> this.name}


