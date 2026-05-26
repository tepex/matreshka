package com.habitloop.app.matreshka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
fun SplashScreen() {
    // Фирменный красный цвет из Figma Properties
    val brandRed = Color(0xFFF10D30)
    val circleWhite = Color.White

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = brandRed // Заливаем весь экран красным
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Графический паттерн из кругов (Логотип YouPlan)
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Первый ряд (3 круга)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = circleWhite.copy(alpha = 0.85f),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                            )
                        }
                    }

                    // Второй ряд (3 круга, центральный подсвечен ярче или прозрачнее по макету)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        repeat(3) { index ->
                            // На макете правый нижний круг имеет чуть другую прозрачность
                            val alphaValue = if (index == 2) 0.5f else 0.85f
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = circleWhite.copy(alpha = alphaValue),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Текстовое название приложения YouPlan
                Text(
                    text = "YouPlan",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.5).sp
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
