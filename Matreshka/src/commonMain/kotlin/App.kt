package com.habitloop.app.matreshka

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AppScreen {
    SPLASH, WELCOME1, WELCOME2, WELCOME3, LOGIN, HABIT_DAY
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }

    if (currentScreen == AppScreen.SPLASH) {
        LaunchedEffect(Unit) {
            delay(3000)
            currentScreen = AppScreen.WELCOME1
        }
    }

    AppContent(
        currentScreen = currentScreen,
        onScreenChange = { nextScreen -> currentScreen = nextScreen }
    )
}

@Composable
fun AppContent(
    currentScreen: AppScreen,
    onScreenChange: (AppScreen) -> Unit
) {
    MaterialTheme {
        when (currentScreen) {
            AppScreen.SPLASH -> SplashScreen()

            AppScreen.WELCOME1 -> Welcome1Screen(
                onNext = { onScreenChange(AppScreen.WELCOME2) },
                onSkip = { onScreenChange(AppScreen.LOGIN) }
            )

            AppScreen.WELCOME2 -> Welcome2Screen(
                onNext = { onScreenChange(AppScreen.WELCOME3) },
                onSkip = { onScreenChange(AppScreen.LOGIN) }
            )

            AppScreen.WELCOME3 -> Welcome3Screen(
                onNext = { onScreenChange(AppScreen.LOGIN) },
                onSkip = { onScreenChange(AppScreen.LOGIN) }
            )

            AppScreen.LOGIN -> LoginScreen(
                onSuccess = { onScreenChange(AppScreen.HABIT_DAY) }
            )
            AppScreen.HABIT_DAY -> HabitDayScreen()

            /*
            AppScreen.HABIT_DAY -> HabitDayScreen(
                onAddHabitClick = { currentScreen = AppScreen.NEW_HABIT }
            )
            AppScreen.NEW_HABIT -> NewHabitScreen(
                onBackClick = { currentScreen = AppScreen.HABIT_DAY }
            )
            */
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    AppContent(
        currentScreen = AppScreen.WELCOME1,
        onScreenChange = {}
    )
}
