package com.habitloop.app.habit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeLayout(
    pageIndex: Int,                 // Номер страницы (0, 1 или 2)
    title: String,                  // Заголовок экрана
    description: String,            // Подзаголовок
    onNextClick: () -> Unit,        // Действие при клике на "Далее"
    onSkipClick: () -> Unit,        // Действие при клике на "Пропустить"
    extraContent: @Composable () -> Unit = {}, // Для чекбокса на 3-м экране
    illustration: @Composable () -> Unit       // Центральная карточка
) {
    // Палитра цветов из Figma
    val backgroundBlue = Color(0xFF3B638A)
    val textLight = Color.White
    val textSecondary = Color.White.copy(alpha = 0.7f)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя панель с кнопкой "Пропустить"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(onClick = onSkipClick) {
                    Text("Пропустить", color = textSecondary, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Центральная большая иллюстрация
            illustration()

            Spacer(modifier = Modifier.height(40.dp))

            // 2. Блок текстов
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textLight,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                fontSize = 15.sp,
                color = textSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Пружина, прижимающая кнопки вниз

            // Дополнительный контент (для чекбокса на Welcome3)
            extraContent()

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Кнопка "Далее"
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Далее", color = backgroundBlue, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Индикатор страниц (Dots)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val isActive = index == pageIndex
                    Box(
                        modifier = Modifier
                            .size(if (isActive) 10.dp else 8.dp)
                            .background(
                                color = if (isActive) Color.White else Color.White.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
