package com.habitloop.app.habit.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.ui.model.toColor
import com.habitloop.app.habit.ui.model.toIcon
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHabitScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (name: String, iconIndex: Int, colorIndex: Int, daysOfWeek: Set<Int>, reminderTime: String?) -> Unit = { _, _, _, _, _ -> }
) {
    // Палитра базовых цветов по макету Figma
    val backgroundWhite = Color(0xFFFFFFFF)
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val grayField = Color(0xFFF4F4F6)
    val brandBlue = Color(0xFF3B638A)
    val errorRed = Color(0xFFE53935)

    // Стейты элементов формы
    var habitName by remember { mutableStateOf("") }
    var selectedIconIndex by remember { mutableStateOf(0) }
    var selectedColorIndex by remember { mutableStateOf(0) }
    var isEveryday by remember { mutableStateOf(true) }
    var isReminderEnabled by remember { mutableStateOf(true) }

    // Стейты времени напоминания и выбранных дней (1 = Пн, 7 = Вс)
    var selectedHour by remember { mutableStateOf("09") }
    var selectedMinute by remember { mutableStateOf("00") }
    var selectedDays by remember { mutableStateOf(setOf(1, 2, 3, 4, 5, 6, 7)) }

    // Контроль открытия выпадающих списков времени
    var isHourMenuExpanded by remember { mutableStateOf(false) }
    var isMinuteMenuExpanded by remember { mutableStateOf(false) }

    val formattedTime = "$selectedHour:$selectedMinute"

    // Списки для генерации меню
    val hoursList = (0..23).map { it.toString().padStart(2, '0') }
    val minutesList = listOf(0, 15, 30, 45).map { it.toString().padStart(2, '0') }

    // TODO: move to mapper.kt
    val daysOfWeek = listOf(
        1 to "Пн", 2 to "Вт", 3 to "Ср", 4 to "Чт", 5 to "Пт", 6 to "Сб", 7 to "Вс"
    )

    // Валидация всей формы на лету
    val isNameValid = habitName.isNotBlank()
    val isDaysSelectionValid = isEveryday || selectedDays.isNotEmpty()
    val isFormValid = isNameValid && isDaysSelectionValid

    Scaffold(
        containerColor = backgroundWhite,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onBackClick() }
                        .padding(8.dp)
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
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Новая привычка",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Название привычки
            Text(text = "Название", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = habitName,
                onValueChange = { if (it.length <= 40) habitName = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                isError = !isNameValid,
                trailingIcon = {
                    Text(
                        text = "${habitName.length}/40",
                        color = if (isNameValid) textGray else errorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = grayField,
                    unfocusedContainerColor = grayField,
                    errorContainerColor = grayField,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = errorRed
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Селектор Иконки
            Text(text = "Иконки", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Habit.Data.IconType.entries.forEachIndexed { i, type ->
                    val isSelected = i == selectedIconIndex
                    Box(
                        modifier = Modifier
                            .weight(1f) // Каждый элемент занимает равную долю ширины
                            .height(40.dp), // Фиксируем только высоту, ширина высчитывается сама
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = if (isSelected) brandBlue else grayField,
                                    shape = CircleShape
                                )
                                .clickable { selectedIconIndex = i },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = type.toIcon(),
                                contentDescription = null,
                                tint = if (isSelected) Color.White else brandBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }


            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Селектор Цвета
            Text(text = "Цвет акцента", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Habit.Data.ColorType.entries.forEachIndexed { i, typeColor ->
                    val isSelected = i == selectedColorIndex
                    // TODO: пока так
                    val isColorLight = typeColor == Habit.Data.ColorType.COLOR3

                    Box(
                        modifier = Modifier
                            .weight(1f) // Равномерное распределение по ширине
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp)
                                .background(color = typeColor.toColor(), shape = CircleShape)
                                .clickable { selectedColorIndex = i },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier.size(16.dp)
                                        .background(
                                            color =
                                                if (isColorLight) Color.Black.copy(alpha = 0.2f) else Color.White,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 4. Повторение (Кастомный Radio-переключатель)
            Text(text = "Частота", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.width(300.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Вариант 1: Ежедневно
                Box(
                    modifier = Modifier
                        //.width(120.dp)
                        .weight(1f)
                        .background(
                            color = if (isEveryday) brandBlue else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            isEveryday = true
                            selectedDays = setOf(1, 2, 3, 4, 5, 6, 7)
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ежедневно",
                        fontSize = 14.sp,
                        fontWeight = if (isEveryday) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isEveryday) Color.White else textDark
                    )
                }

                Spacer(modifier = Modifier.width(40.dp))

                // Вариант 2: По дням недели
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (!isEveryday) brandBlue else grayField,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            isEveryday = false
                            selectedDays = emptySet() // Сбрасываем для ручного выбора
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "По дням недели",
                        fontSize = 14.sp,
                        fontWeight = if (!isEveryday) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (!isEveryday) Color.White else textDark
                    )
                }
            }

            // Анимированная панель выбора дней недели
            AnimatedVisibility(
                visible = !isEveryday,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Выберите дни недели",
                        fontSize = 14.sp,
                        color = if (isDaysSelectionValid) textGray else errorRed,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        daysOfWeek.forEach { (dayNumber, dayName) ->
                            val isDaySelected = selectedDays.contains(dayNumber)
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = if (isDaySelected) brandBlue else grayField,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        val updatedDays = selectedDays.toMutableSet()
                                        if (isDaySelected) updatedDays.remove(dayNumber) else updatedDays.add(dayNumber)
                                        selectedDays = updatedDays
                                        if (updatedDays.size == 7) isEveryday = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDaySelected) Color.White else textDark
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            /*

            // 5. Напоминания и выбор Времени (Кроссплатформенный Dropdown)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Напоминание", fontSize = 16.sp, color = textDark, fontWeight = FontWeight.SemiBold)
                    Text(text = "Получать пуш-уведомления", fontSize = 13.sp, color = textGray)
                }

                Switch(
                    checked = isReminderEnabled,
                    onCheckedChange = { isReminderEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = brandBlue,
                        uncheckedThumbColor = textGray,
                        uncheckedTrackColor = grayField
                    )
                )
            }

            AnimatedVisibility(
                visible = isReminderEnabled,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .background(grayField, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Время",
                                tint = brandBlue,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(text = "Время напоминания", fontSize = 15.sp, color = textDark)
                        }

                        // Селекторы часов и минут через чистые DropdownMenu
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box {
                                Row(modifier = Modifier.clickable { isHourMenuExpanded = true }
                                    .padding(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = selectedHour, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = brandBlue)
                                    Icon(Icons.Default.ArrowDropDown, null, tint = brandBlue, modifier = Modifier.size(18.dp))
                                }
                                DropdownMenu(
                                    expanded = isHourMenuExpanded,
                                    onDismissRequest = { isHourMenuExpanded = false }
                                ) {
                                    hoursList.forEach { hour ->
                                        DropdownMenuItem(
                                            text = { Text(hour) },
                                            onClick = {
                                                selectedHour = hour
                                                isHourMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Text(text = ":",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = brandBlue,
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )

                            Box {
                                Row(modifier = Modifier.clickable { isMinuteMenuExpanded = true }
                                    .padding(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = selectedMinute, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = brandBlue)
                                    Icon(Icons.Default.ArrowDropDown, null, tint = brandBlue, modifier = Modifier.size(18.dp))
                                }
                                DropdownMenu(
                                    expanded = isMinuteMenuExpanded,
                                    onDismissRequest = { isMinuteMenuExpanded = false }
                                ) {
                                    minutesList.forEach { minute ->
                                        DropdownMenuItem(
                                            text = { Text(minute) },
                                            onClick = {
                                                selectedMinute = minute
                                                isMinuteMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }*/

            Spacer(modifier = Modifier.height(40.dp))

            // 6. Кнопка создания
            Button(
                onClick = {
                    if (isFormValid) {
                        val finalReminderTime = if (isReminderEnabled) formattedTime else null
                        onSaveClick(habitName.trim(), selectedIconIndex, selectedColorIndex, selectedDays, finalReminderTime)
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = brandBlue,
                    disabledContainerColor = brandBlue.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = "Создать привычку",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
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
