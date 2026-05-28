package com.habitloop.app.matreshka.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Welcome2Screen(onNext: () -> Unit, onSkip: () -> Unit) {
    WelcomeLayout(
        pageIndex = 1,
        title = "Строй серии",
        description = "Каждый выполненный день удлиняет серию. Не прерывай – и наблюдай за ростом",
        onNextClick = onNext,
        onSkipClick = onSkip
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(Color(0xFFF10D30), shape = RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Верхняя плашка: Огонек + цифры 0050
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        "0050".forEach { char ->
                            Box(
                                modifier = Modifier.size(width = 24.dp, height = 32.dp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(char.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }

                // Нижняя часть: Иконка графика + строчки списка справа
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(
                        modifier = Modifier.size(54.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        repeat(3) { index ->
                            val widthSize = if (index == 1) 60.dp else 40.dp
                            Box(modifier = Modifier.size(width = widthSize, height = 8.dp).background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(4.dp)))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Welcome2ScreenPreview() {
    Welcome2Screen(onNext = {}, onSkip = {})
}
