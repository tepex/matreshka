package com.habitloop.app.matreshka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Welcome1Screen(onNext: () -> Unit, onSkip: () -> Unit) {
    WelcomeLayout(
        pageIndex = 0,
        title = "Отслеживай привычки",
        description = "Добавь то, что хочешь делать каждый день – и просто отмечай",
        onNextClick = onNext,
        onSkipClick = onSkip
    ) {
        // Иллюстрация: Красный блок с элементами
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(Color(0xFFF10D30), shape = RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                // Иконка поиска слева вверху
                Box(
                    modifier = Modifier.size(54.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                }

                // Иконка обновления слева внизу
                Box(
                    modifier = Modifier.size(54.dp).background(Color.White.copy(alpha = 0.2f), CircleShape).align(Alignment.BottomStart),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                }

                // Галочки справа (список привычек)
                Column(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Welcome1ScreenPreview() {
    Welcome1Screen(onNext = {}, onSkip = {})
}
