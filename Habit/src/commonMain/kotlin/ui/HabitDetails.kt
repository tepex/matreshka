package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitloop.app.habit.DI
import com.habitloop.app.habit.domain.HabitFactRepository
import com.habitloop.app.habit.domain.HabitRepository
import com.habitloop.app.habit.domain.getHabitStatistics
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitStatistics
import com.habitloop.app.habit.ui.model.getDaysWord
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailsScreen(
    habitId: Habit.Id = Habit.Id.INVALID,
    habitRepository: HabitRepository,
    habitFactRepository: HabitFactRepository,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    // Цветовая палитра из Figma
    val bgLightBlue = Color(0xFFF4F7FA)
    val backgroundWhite = Color(0xFFFFFFFF)
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandRed = Color(0xFFE53935)  // Красный цвет привычки "Медитация"
    val brandBlue = Color(0xFF3B638A) // Цвет кнопок и процентов
    val grayField = Color(0xFFF4F4F6)

    val habit = remember { habitRepository.getHabit(habitId).getOrNull() }
    val today = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    val stats = habit
        ?.let { remember(it.id) { getHabitStatistics(it, habitFactRepository, today) } }
        ?: HabitStatistics()

    println("[HabitDetails] habit: $habit")
    println("[HabitDetails] stats: $stats")

    Scaffold(
        containerColor = backgroundWhite,
        topBar = {
            TopAppBar(
                title = { Text(text = "Назад") },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                            .background(Color.White, shape = CircleShape)
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = textDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .background(bgLightBlue)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Верхний блок: Информация о привычке
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(brandRed, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧘", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = habit?.data?.name?.value ?: "Not found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                        /*
                        Spacer(modifier = Modifier.height(2.dp))

                        // Строка рекорда
                        Text(
                            text = "${stats.currentStreak} ${stats.currentStreak.getDaysWord()} подряд - рекорд!",
                            fontSize = 13.sp,
                            color = textGray
                        )*/
                    }
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, shape = CircleShape)
                        .border(1.dp, textGray, shape = CircleShape)
                        .clickable { onEditClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать",
                        tint = textGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Блок числовых показателей статистики (Текущая, Лучшая, За 30 дней)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Числа выводятся без эмодзи согласно требованиям
                    StatItem(value = "${stats.currentStreak}", label = "Текущая", valueColor = brandBlue)
                    StatItem(value = "${stats.bestStreak}", label = "Лучшая", valueColor = brandRed)
                    StatItem(value = "${stats.successRate30Days}%", label = "За 30 дней", valueColor = Color(0xFFFFB300))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Симуляция: Тепловая карта (Heatmap) - оставлена без изменений
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Тепловая карта", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "7 дней", fontSize = 12.sp, color = textGray)
                            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = textGray, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        val days = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
                        days.forEach { day ->
                            Text(text = day, fontSize = 11.sp, color = textGray, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        for (i in 0..6) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .aspectRatio(1f)
                                    .background(if (i % 2 == 0) brandBlue else Color(0xFFE5E5EA), shape = CircleShape)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Симуляция: Выполнение по дням недели - оставлена без изменений
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = "Выполнение по дням недели", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = null,
                                tint = textGray,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(text = "23-29 марта", fontSize = 12.sp, color = textGray, modifier = Modifier.padding(horizontal = 4.dp))
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = textGray,
                                modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "25%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = brandBlue)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val progress = listOf(0.2f, 0.5f, 0.1f, 0.8f, 0.4f, 0.9f, 0.3f)
                        progress.forEach { value ->
                            Box(
                                modifier = Modifier.weight(1f)
                                    .padding(horizontal = 6.dp)
                                    .fillMaxHeight(value)
                                    .background(brandBlue, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val days = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
                    days.forEach { day ->
                        Text(
                            text = day,
                            fontSize = 11.sp,
                            color = textGray,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Симуляция: Заморозки - оставлена без изменений
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "❄️", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Заморозки: 2 из 3 доступно", fontSize = 14.sp, color = textDark)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 6. Симуляция: Кнопка архивации - оставлена без изменений
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E5EA)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Архивировать привычку", color = brandRed, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Вспомогательный компонент для элемента статистики
@Composable
fun StatItem(value: String, label: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Black, color = valueColor)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview
@Composable
fun HabitDetailsScreenPreview() {
    val di = DI()
    HabitDetailsScreen(
        habitRepository = di.habitRepository,
        habitFactRepository = di.habitFactRepository
    )
}
