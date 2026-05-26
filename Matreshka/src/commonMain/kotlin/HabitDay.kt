package com.habitloop.app.matreshka

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

// Модель данных для привычки
data class HabitItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val isCompleted: Boolean
)

@Composable
fun HabitDayScreen(
    onAddHabitClick: () -> Unit = {},
    onHabitClick: (Int) -> Unit = {}
) {
    // Палитра цветов по макету Figma
    val bgLightBlue = Color(0xFFF4F7FA)
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandBlue = Color(0xFF3B638A)

    // Тестовый список привычек из макета
    val habits = listOf(
        HabitItem(1, "Утренняя пробежка", "11 дней", Icons.AutoMirrored.Filled.DirectionsRun, Color(0xFF0066CC), true),
        HabitItem(2, "Чтение 30 мин", "5 дней", Icons.AutoMirrored.Filled.MenuBook, Color(0xFF1C1C1E), true),
        HabitItem(3, "Медитация", "31 день подряд - рекорд!", Icons.Default.SelfImprovement, Color(0xFFE53935), false),
        HabitItem(4, "Пить воду 2л", "0 дней", Icons.Default.LocalCafe, Color(0xFFFFB300), false)
    )

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
                    onClick = {},
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
                    onClick = {},
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("СТАТИСТИКА", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
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
                    Text(text = "Четверг, 2 апреля", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
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
                val days = listOf("30" to "Пн", "31" to "Вт", "1" to "Ср", "2" to "Чт", "3" to "Пт", "4" to "Сб", "5" to "Вс")
                days.forEach { (date, dayName) ->
                    val isSelected = date == "2" // Четверг по макету
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
                items(habits) { habit ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onHabitClick(habit.id) }
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
