package com.habitloop.app.matreshka.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NewHabitScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    // Палитра базовых цветов по макету
    val backgroundWhite = Color(0xFFFFFFFF)
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val grayField = Color(0xFFF4F4F6)
    val brandBlue = Color(0xFF3B638A)

    // Стейты элементов формы
    var habitName by remember { mutableStateOf("Утренняя пробежка") }
    var selectedIconIndex by remember { mutableStateOf(0) }
    var selectedColorIndex by remember { mutableStateOf(0) }
    var isEveryday by remember { mutableStateOf(true) }
    var isReminderEnabled by remember { mutableStateOf(true) }

    // Доступные иконки из Figma
    val iconsList = listOf(
        Icons.AutoMirrored.Filled.DirectionsRun,
        Icons.AutoMirrored.Filled.MenuBook,
        Icons.Default.SelfImprovement,
        Icons.Default.LocalCafe,
        Icons.Default.FitnessCenter,
        Icons.Default.ShoppingCart
    )

    // Доступные цвета из Figma палитры
    val colorsList = listOf(
        Color(0xFF0066CC), // Синий
        Color(0xFFE53935), // Красный
        Color(0xFFFFB300), // Желтый
        Color(0xFF4CAF50), // Зеленый
        Color(0xFF1C1C1E), // Темно-серый
        Color(0xFF9C27B0)  // Фиолетовый
    )

    Scaffold(
        containerColor = backgroundWhite,
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
                .verticalScroll(rememberScrollState()), // Включаем скролл для маленьких экранов
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Новая привычка",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Поле ввода Названия
            Text(text = "Название", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = habitName,
                onValueChange = { if (it.length <= 40) habitName = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    Text(
                        text = "${habitName.length}/40",
                        color = textGray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grayField,
                    unfocusedContainerColor = grayField,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Горизонтальный выбор Иконки
            Text(text = "Иконки", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                iconsList.forEachIndexed { index, icon ->
                    val isSelected = index == selectedIconIndex
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = if (isSelected) brandBlue else grayField,
                                shape = CircleShape
                            )
                            .clickable { selectedIconIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else brandBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Горизонтальный выбор Цвета
            Text(text = "Цвет акцента", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                colorsList.forEachIndexed { index, color ->
                    val isSelected = index == selectedColorIndex
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(color = color, shape = CircleShape)
                            .clickable { selectedColorIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color.White, shape = CircleShape)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Выбор Частоты (Вкладки Ежедневно / По дням недели)
            Text(text = "Частота", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(grayField, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                // Вкладка "Ежедневно"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .background(
                            color = if (isEveryday) brandBlue else Color.Transparent,
                            shape = RoundedCornerShape(11.dp)
                        )
                        .clickable { isEveryday = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ежедневно",
                        color = if (isEveryday) Color.White else textDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                // Вкладка "По дням недели"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .background(
                            color = if (!isEveryday) brandBlue else Color.Transparent,
                            shape = RoundedCornerShape(11.dp)
                        )
                        .clickable { isEveryday = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "По дням недели",
                        color = if (!isEveryday) Color.White else textDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Блок напоминания
            Text(text = "Напоминание", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(grayField, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Включить",
                        color = textDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(
                        checked = isReminderEnabled,
                        onCheckedChange = { isReminderEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = brandBlue,
                            uncheckedThumbColor = textGray,
                            uncheckedTrackColor = Color.White
                        )
                    )
                }
                // Кнопка выбора времени по макету
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = textGray, modifier = Modifier.size(16.dp))
                    Text(text = "08:30", color = textDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            // 6. Главная кнопка Сохранить
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brandBlue)
            ) {
                Text(text = "Сохранить", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
fun NewHabitScreenPreview() {
    NewHabitScreen()
}
