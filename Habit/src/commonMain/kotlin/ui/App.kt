package com.habitloop.app.habit.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.habitloop.app.habit.DI
import com.habitloop.app.habit.domain.createNewHabit
import com.habitloop.app.habit.domain.model.Habit
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AppScreen {
    SPLASH, WELCOME1, WELCOME2, WELCOME3, LOGIN, HABIT_DAY, NEW_HABIT, HABIT_DETAILS, STATISTICS, SETTINGS
}

@Composable
fun App() {
    val di = remember { DI() }
    val settings = remember { com.russhwolf.settings.Settings() }
    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
    var selectedHabitId by remember { mutableStateOf(Habit.Id.INVALID) }

    if (currentScreen == AppScreen.SPLASH) {
        LaunchedEffect(Unit) {
            delay(3000)

            val isLogged = settings.getBoolean("is_logged_in", false)
            currentScreen = if (isLogged) AppScreen.HABIT_DAY else AppScreen.WELCOME1
        }
    }

    AppContent(
        di,
        currentScreen = currentScreen,
        selectedHabitId = selectedHabitId,
        onScreenChange = { nextScreen -> currentScreen = nextScreen },
        onHabitSelected = { id ->
            selectedHabitId = id
            currentScreen = AppScreen.HABIT_DETAILS
        }
    )
}

@Composable
fun AppContent(
    di: DI,
    currentScreen: AppScreen,
    selectedHabitId: Habit.Id,
    onScreenChange: (AppScreen) -> Unit,
    onHabitSelected: (Habit.Id) -> Unit
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
                di.habitRepository,
                di.habitFactRepository,
                onAddHabitClick = { onScreenChange(AppScreen.NEW_HABIT) },
                onHabitClick = { id -> onHabitSelected(id) },
                onTabClick = { tabScreen -> onScreenChange(tabScreen) }
            )
            AppScreen.NEW_HABIT -> NewHabitScreen(
                onBackClick = { onScreenChange(AppScreen.HABIT_DAY) },
                onSaveClick = { newHabit ->
                    createNewHabit(
                        habit = newHabit,
                        habitRepository = di.habitRepository
                    )
                    onScreenChange(AppScreen.HABIT_DAY)
                }
            )
            AppScreen.HABIT_DETAILS ->
                if (selectedHabitId != Habit.Id.INVALID)
                    HabitDetailsScreen(
                        habitId = selectedHabitId,
                        onBackClick = { onScreenChange(AppScreen.HABIT_DAY) }
                    )
                else onScreenChange(AppScreen.HABIT_DAY)

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
        DI(),
        currentScreen = AppScreen.WELCOME1,
        Habit.Id.INVALID,
        onScreenChange = {},
        {}
    )
}
