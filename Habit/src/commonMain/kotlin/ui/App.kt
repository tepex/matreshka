package com.habitloop.app.habit.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AppScreen {
    SPLASH, WELCOME1, WELCOME2, WELCOME3, LOGIN, HABIT_DAY, NEW_HABIT, HABIT_DETAILS, STATISTICS, SETTINGS
}

@Composable
fun App() {
    val settings = remember { com.russhwolf.settings.Settings() }
    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }

    if (currentScreen == AppScreen.SPLASH) {
        LaunchedEffect(Unit) {
            delay(3000)

            val isLogged = settings.getBoolean("is_logged_in", false)
            currentScreen = if (isLogged) AppScreen.HABIT_DAY else AppScreen.WELCOME1
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

            AppScreen.HABIT_DAY -> HabitDayScreen(
                onAddHabitClick = { onScreenChange(AppScreen.NEW_HABIT) },
                onHabitClick = { onScreenChange(AppScreen.HABIT_DETAILS) },
                onTabClick = { tabScreen -> onScreenChange(tabScreen) }
            )
            AppScreen.NEW_HABIT -> NewHabitScreen(
                onBackClick = { onScreenChange(AppScreen.HABIT_DAY) },
                onSaveClick = { onScreenChange(AppScreen.HABIT_DAY) }
            )
            AppScreen.HABIT_DETAILS -> HabitDetailsScreen(
                onBackClick = { onScreenChange(AppScreen.HABIT_DAY) },
                onEditClick = { onScreenChange(AppScreen.NEW_HABIT) }
            )

            AppScreen.STATISTICS -> StatisticsScreen(
                onTabClick = { tabScreen -> onScreenChange(tabScreen) }
            )
            AppScreen.SETTINGS -> SettingsScreen(
                onTabClick = { tabScreen -> onScreenChange(tabScreen) },
                onLogoutSuccess = { onScreenChange(AppScreen.LOGIN) } // Перенаправляет на чистый ввод телефона при логауте/удалении
            )
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
