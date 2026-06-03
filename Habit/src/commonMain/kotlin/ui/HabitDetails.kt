package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.habitloop.app.habit.domain.getHeatmapStatistics
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitStatistics
import com.habitloop.app.habit.domain.model.HeatmapPeriod
import com.habitloop.app.habit.domain.model.HeatmapState
import com.habitloop.app.habit.ui.model.HeatmapUiState
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

    // По умолчанию ставим 28 дней (WEEK4) как в макетах
    var selectedPeriod by remember { mutableStateOf(HeatmapPeriod.WEEK4) }
    var isPeriodMenuExpanded by remember { mutableStateOf(false) }

    val heatmapUiState = habit?.let {
        remember(it.id, selectedPeriod) {
            getHeatmapStatistics(it, habitFactRepository, today, selectedPeriod)
        }
    } ?: HeatmapUiState(emptyList())

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
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .background(bgLightBlue)
                .verticalScroll(scrollState)
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

            // 3. Интегрированная Тепловая карта (Heatmap)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Заголовок секции и выпадающий список выбора периода
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Тепловая карта",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )

                        // Дропдаун для выбора количества дней анализа
                        Box {
                            val periodText = when (selectedPeriod) {
                                HeatmapPeriod.WEEK1 -> "7 дней"
                                HeatmapPeriod.WEEK2 -> "14 дней"
                                HeatmapPeriod.WEEK4 -> "28 дней"
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isPeriodMenuExpanded = true }
                            ) {
                                Text(text = periodText, fontSize = 12.sp, color = textGray)
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Выбрать период",
                                    tint = textGray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = isPeriodMenuExpanded,
                                onDismissRequest = { isPeriodMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("7 дней") },
                                    onClick = {
                                        selectedPeriod = HeatmapPeriod.WEEK1
                                        isPeriodMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("14 дней") },
                                    onClick = {
                                        selectedPeriod = HeatmapPeriod.WEEK2
                                        isPeriodMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("28 дней") },
                                    onClick = {
                                        selectedPeriod = HeatmapPeriod.WEEK4
                                        isPeriodMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Статичная шапка дней недели (Пн-Вс)
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // Отрисовка динамической матрицы строк тепловой карты
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        heatmapUiState.rows.forEach { row ->
                            HeatmapWeekRow(row = row, brandBlue = brandBlue)
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

@Composable
fun HeatmapWeekRow(
    row: com.habitloop.app.habit.ui.model.HeatmapRow,
    brandBlue: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        row.cells.forEach { cellState ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    // Дополнительный padding уменьшает размер самого кружка внутри сетки
                    .padding(16.dp)
                    .background(
                        color = when (cellState) {
                            HeatmapState.COMPLETED -> brandBlue
                            HeatmapState.NOT_COMPLETED -> Color(0xFFE5E5EA)
                            HeatmapState.EMPTY -> Color.Transparent
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview
@Composable
fun HeatmapWeekRowPreview() {
    val sampleRow = com.habitloop.app.habit.ui.model.HeatmapRow(
        cells = listOf(
            HeatmapState.COMPLETED,
            HeatmapState.NOT_COMPLETED,
            HeatmapState.EMPTY,
            HeatmapState.COMPLETED,
            HeatmapState.NOT_COMPLETED,
            HeatmapState.EMPTY,
            HeatmapState.EMPTY
        )
    )
    Surface(color = Color.White, modifier = Modifier.padding(16.dp)) {
        HeatmapWeekRow(row = sampleRow, brandBlue = Color(0xFF3B638A))
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
