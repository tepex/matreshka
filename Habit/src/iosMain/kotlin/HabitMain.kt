package com.habitloop.app.habit

import androidx.compose.ui.window.ComposeUIViewController
import com.habitloop.app.habit.ui.App
import com.habitloop.app.habit.ui.LoginScreen
import com.russhwolf.settings.Settings
import platform.UIKit.UIViewController

fun HabitViewController(): UIViewController = ComposeUIViewController {
    //LoginScreen(onSuccess = onSuccess)
    App()
}
fun isUserLoggedIn(): Boolean =
    Settings().getBoolean("is_logged_in", false)
