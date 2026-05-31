package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitloop.app.habit.DI
import com.habitloop.app.habit.domain.HabitFactRepository
import com.habitloop.app.habit.domain.HabitRepository
import com.habitloop.app.habit.domain.getHabitFactsForDate
import com.habitloop.app.habit.domain.model.HabitFactAggregate
import com.habitloop.app.habit.ui.model.HabitItem
import com.habitloop.app.habit.ui.model.toHabitItem
import com.habitloop.app.habit.ui.model.toRu
import kotlinx.datetime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

val bgLightBlue = Color(0xFFF4F7FA)
val textDark = Color(0xFF1A1A1A)
val textGray = Color(0xFF8A8A8E)
val brandBlue = Color(0xFF3B638A)

@Composable
fun HabitDayScreen(
    habitRepository: HabitRepository,
    habitFactRepository: HabitFactRepository,
    onAddHabitClick: () -> Unit = {},
    onHabitClick: (Int) -> Unit = {},
    onTabClick: (AppScreen) -> Unit = {}
) {

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
        ).map { aggregate -> aggregate.toHabitItem() }
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
                modifier = Modifier
                    .size(56.dp)
                    .padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Добавить привычку",
                    modifier = Modifier.size(28.dp)
                )
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
            HabitWeekCalendar(
                selectedDate = selectedDate,
                today = today,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Список привычек дня
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(habitsList) { habitItem ->
                    HabitCard(habitItem, onHabitClick)
                }
            }
        }
    }
}

// Компонент карточки привычки, адаптированный под HabitFactAggregate
@Composable
fun HabitCard(
    habitItem: HabitItem,
    onHabitClick: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onHabitClick(habitItem.id) },
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
            // Цветной круг с иконкой привычки слева
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(brandBlue.copy(alpha = 0.12f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = habitItem.icon,
                    contentDescription = null,
                    tint = habitItem.iconBgColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Текстовая секция: Название привычки и серия дней
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habitItem.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "x days", // TODO: доделать
                    // Динамически собираем текст серии с правильным склонением слова "день"
                    // text = "🔥 ${habitItem.streak} ${getDaysWord(habitItem.streak)}",
                    fontSize = 13.sp,
                    color = textGray
                )
            }

            // Круглая кнопка-чекбокс отметки выполнения
            IconButton(
                onClick = {
                    // TODO: Инвертировать статус факта выполнения через интерактор
                    ++stateUpdateTrigger
                },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (habitItem.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Отметить выполнение",
                    tint = if (habitItem.isCompleted) brandBlue else Color(0xFFBCC2CD),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun HabitDayScreenPreview() {
    // Предоставляем фейковые репозитории в экран
    val di = DI()
    HabitDayScreen(
        habitRepository = di.habitRepository,
        habitFactRepository = di.habitFactRepository,
        onAddHabitClick = {},
        onHabitClick = {},
        onTabClick = {}
    )
}
