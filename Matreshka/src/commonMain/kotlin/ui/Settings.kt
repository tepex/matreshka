package com.habitloop.app.matreshka.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onTabClick: (AppScreen) -> Unit = {},
    onLogoutSuccess: () -> Unit = {} // Коллбэк для сброса на экран логина
) {
    // Палитра из Figma
    val bgLightBlue = Color(0xFFF4F7FA)
    val backgroundWhite = Color(0xFFFFFFFF)
    val textDark = Color(0xFF1A1A1A)
    val textGray = Color(0xFF8A8A8E)
    val brandBlue = Color(0xFF3B638A)
    val brandRed = Color(0xFFE53935)
    val grayField = Color(0xFFF4F4F6)

    // Состояния полей и диалогов
    var nameText by remember { mutableStateOf("Алина") }
    var showLogoutDialog by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                    selected = false,
                    onClick = { onTabClick(AppScreen.STATISTICS) },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("СТАТИСТИКА", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { onTabClick(AppScreen.SETTINGS) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("НАСТРОЙКИ", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = brandBlue, selectedTextColor = brandBlue, indicatorColor = Color.Transparent)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя панель с кнопкой "Назад"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onTabClick(AppScreen.HABIT_DAY) }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = brandBlue)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Назад", color = brandBlue, fontSize = 16.sp)
                }
            }

            Text(
                text = "Настройки",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textDark,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Блок Аватарки с кнопкой камеры
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.size(100.dp)) {
                // Круглый контейнер под фото
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFDCDCE2), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = textGray, modifier = Modifier.size(54.dp))
                }
                // Маленькая кнопка смены фото (Камера) из Figma
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(brandBlue, shape = CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "255 дней хороших привычек", fontSize = 13.sp, color = textGray)

            Spacer(modifier = Modifier.height(24.dp))

            // Поле изменения имени
            TextField(
                value = nameText,
                onValueChange = { nameText = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundWhite,
                    unfocusedContainerColor = backgroundWhite,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Сохранить" изменения профиля
            Button(
                onClick = { onTabClick(AppScreen.HABIT_DAY) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brandBlue)
            ) {
                Text("Сохранить", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.weight(1f)) // Отжимает деструктивные кнопки вниз

            // Кнопка "Выйти из приложения"
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C435A)) // Серо-синий оттенок из макета
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Выйти из приложения", color = Color.White, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Текстовая кнопка "Удалить аккаунт"
            TextButton(onClick = { showDeleteDialog = true }) {
                Text("Удалить аккаунт", color = brandRed, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // --- МОДАЛЬНЫЙ ДИАЛОГ: ВЫХОД ИЗ ПРИЛОЖЕНИЯ ---
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutSuccess() // Вызов сброса стейта
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = brandBlue)
                ) {
                    Text("Выйти", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = textGray,
                        modifier = Modifier.clickable { showLogoutDialog = false }.size(24.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Вы действительно хотите выйти из приложения?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    // --- МОДАЛЬНЫЙ ДИАЛОГ: УДАЛЕНИЕ АККАУНТА ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onLogoutSuccess()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = brandRed) // Акцентный красный
                ) {
                    Text("Удалить аккаунт", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = textGray,
                        modifier = Modifier.clickable { showDeleteDialog = false }.size(24.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Вы действительно хотите удалить аккаунт?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
