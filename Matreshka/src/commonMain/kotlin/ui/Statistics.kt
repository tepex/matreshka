package com.habitloop.app.matreshka.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatisticsScreen(
    onTabClick: (AppScreen) -> Unit = {}
) {
    // Палитра из Figma Properties
    val bgLightBlue = Color(0xFFF4F7FA)
    val backgroundWhite = Color(0xFFFFFFFF)
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandBlue = Color(0xFF3B638A)
    val grayField = Color(0xFFF4F4F6)

    var selectedPeriod by remember { mutableStateOf(1) } // 0 - Неделя, 1 - Месяц, 2 - Все время

    Scaffold(
        containerColor = bgLightBlue,
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    selected = false,
                    onClick = { onTabClick(AppScreen.HABIT_DAY) },
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    label = { Text("СЕГОДНЯ", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { onTabClick(AppScreen.STATISTICS) },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("СТАТИСТИКА", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = brandBlue, selectedTextColor = brandBlue, indicatorColor = Color.Transparent)
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Статистика", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textDark)

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Переключатель периодов (Неделя / Месяц / Все время)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(grayField, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                val periods = listOf("Неделя", "Месяц", "Все время")
                periods.forEachIndexed { index, text ->
                    val isSelected = index == selectedPeriod
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .background(
                                color = if (isSelected) brandBlue else Color.Transparent,
                                shape = RoundedCornerShape(11.dp)
                            )
                            .clickable { selectedPeriod = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            color = if (isSelected) Color.White else textDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Карточка "Общий % выполнения"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundWhite)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Общий %", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text(text = "выполнения за месяц", fontSize = 13.sp, color = textGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Маленькие иконки наград внутри карточки
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(20.dp))
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                        }
                    }

                    // Круговой прогресс-бар с цифрой 87% из макета
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(70.dp)) {
                        CircularProgressIndicator(
                            progress = { 0.87f },
                            modifier = Modifier.fillMaxSize(),
                            color = Color(0xFFE53935), // Розово-красный из Figma
                            trackColor = grayField,
                            strokeWidth = 6.dp
                        )
                        Text(text = "87%", fontSize = 18.sp, fontWeight = FontWeight.Black, color = textDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Секция "Рейтинг привычек"
            Text(text = "Рейтинг привычек", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textGray)
            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                RatingHabitItem("Утренняя пробежка", "93%", Icons.AutoMirrored.Filled.DirectionsRun, Color(0xFF0066CC))
                RatingHabitItem("Медитация", "80%", Icons.Default.SelfImprovement, Color(0xFFE53935))
                RatingHabitItem("Пить воду 2л", "50%", Icons.Default.LocalCafe, Color(0xFFFFB300))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Секция "Тренд активности" (Линейный волнообразный график)
            Text(text = "Тренд активности (30 дн.)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textGray)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Активность растет", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        Text(text = "📈 1-26 марта", fontSize = 12.sp, color = textGray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Рисуем волну (Bezier Curve) через Canvas JetBrains Compose
                    Canvas(modifier = Modifier.fillMaxWidth().height(70.dp)) {
                        val width = size.width
                        val height = size.height

                        val path = Path().apply {
                            moveTo(0f, height * 0.7f)
                            cubicTo(width * 0.25f, height * 0.8f, width * 0.4f, height * 0.2f, width * 0.6f, height * 0.4f)
                            cubicTo(width * 0.75f, height * 0.5f, width * 0.9f, height * 0.1f, width, height * 0.15f)
                        }

                        drawPath(
                            path = path,
                            color = brandBlue,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Блок "Достижения"
            Text(text = "Достижения", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textGray)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val achievements = listOf(
                    Icons.Default.EmojiEvents to Color(0xFFFFB300),
                    Icons.Default.Stars to Color(0xFF9C27B0),
                    Icons.Default.LocalFireDepartment to Color(0xFFE53935),
                    Icons.Default.WorkspacePremium to Color(0xFF0066CC)
                )
                achievements.forEach { (icon, color) ->
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(grayField, shape = RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Вспомогательный компонент строки рейтинга привычки
@Composable
fun RatingHabitItem(title: String, percentage: String, icon: ImageVector, iconBg: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.size(38.dp).background(iconBg, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1A1A))
            }

            // Процент выполнения справа в кружочке
            Box(
                modifier = Modifier
                    .size(width = 46.dp, height = 28.dp)
                    .background(Color(0xFFF4F7FA), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = percentage, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B638A))
            }
        }
    }
}

@Preview
@Composable
fun StatisticsScreenPreview() {
    StatisticsScreen()
}
