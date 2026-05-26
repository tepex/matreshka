package com.habitloop.app.matreshka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Welcome3Screen(onNext: () -> Unit, onSkip: () -> Unit) {
    var isChecked by remember { mutableStateOf(false) }

    WelcomeLayout(
        pageIndex = 2,
        title = "Напоминай",
        description = "Разреши уведомления – и YouPlan напомнит в нужный момент",
        onNextClick = onNext,
        onSkipClick = onSkip,
        extraContent = {
            // Чекбокс "Больше не показывать" из Figma
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color.White.copy(alpha = 0.6f),
                        checkmarkColor = Color(0xFF3B638A)
                    )
                )
                Text("Больше не показывать", color = Color.White, fontSize = 14.sp)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(Color(0xFFF10D30), shape = RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                // Колокольчик слева вверху
                Box(
                    modifier = Modifier.size(54.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                }

                // Часы Apple Watch справа в центре
                Icon(
                    Icons.Default.Watch,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(72.dp).align(Alignment.CenterEnd)
                )

                // Медитирующий человечек (силуэт) слева внизу
                Icon(
                    Icons.Default.Accessibility,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(54.dp).align(Alignment.BottomStart)
                )
            }
        }
    }
}

@Preview
@Composable
fun Welcome3ScreenPreview() {
    Welcome3Screen(onNext = {}, onSkip = {})
}
