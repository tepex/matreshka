package com.habitloop.app.matreshka

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HabitDetailsScreen(
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

    Scaffold(
        containerColor = bgLightBlue,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onBackClick() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = brandBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Назад", color = brandBlue, fontSize = 16.sp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Шапка привычки: Иконка, название и кнопка редактирования
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(brandRed, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SelfImprovement,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(text = "Медитация", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text(text = "🔥 31 день подряд - рекорд!", fontSize = 13.sp, color = textGray)
                    }
                }

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать", tint = textGray, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Блок Статистики (Текущая, Лучшая, За 30 дней)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundWhite)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem(value = "31", label = "Текущая", valueColor = brandBlue)
                    StatItem(value = "31", label = "Лучшая", valueColor = brandRed)
                    StatItem(value = "87%", label = "За 30 дней", valueColor = Color(0xFFFFB300))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Блок "Тепловая карта"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Тепловая карта", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text(text = "7 дней ▾", fontSize = 12.sp, color = brandBlue, modifier = Modifier.clickable { })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Сетка дней недели (Пн-Вс)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        val weekdays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
                        weekdays.forEach { day ->
                            Text(text = day, fontSize = 11.sp, color = textGray, modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Симуляция заполненных кружков тепловой карты (строки по дням)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(3) { rowIndex ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                repeat(7) { colIndex ->
                                    // Рандомно закрашиваем некоторые дни как выполненные в Figma
                                    val isDone = (rowIndex + colIndex) % 2 == 0
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(
                                                color = if (isDone) brandBlue else grayField,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Блок "Выполнение по дням недели"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Выполнение по дням недели", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text(text = "📅 23-29 марта", fontSize = 12.sp, color = textGray)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "25%", fontSize = 24.sp, fontWeight = FontWeight.Black, color = brandBlue)

                    Spacer(modifier = Modifier.height(16.dp))

                    // График-гистограмма (Вертикальные столбики)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val barHeights = listOf(20.dp, 45.dp, 10.dp, 55.dp, 30.dp, 40.dp, 15.dp)
                        val weekdays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

                        barHeights.forEachIndexed { index, height ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 8.dp, height = height)
                                        .background(brandBlue, RoundedCornerShape(4.dp))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = weekdays[index], fontSize = 10.sp, color = textGray)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 5. Блок "Заморозка"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundWhite)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AcUnit, contentDescription = null, tint = brandBlue, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Заморозок: 2 из 3 доступно", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textDark)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // 6. Кнопка "Архивировать привычку"
            Button(
                onClick = { /* Логика архивации */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = grayField)
            ) {
                Text(text = "Архивировать привычку", color = brandRed, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(24.dp))
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
    HabitDetailsScreen()
}
