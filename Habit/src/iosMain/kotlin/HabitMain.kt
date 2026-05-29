package com.habitloop.app.habit

import androidx.compose.ui.window.ComposeUIViewController
import com.habitloop.app.habit.ui.App
import com.russhwolf.settings.Settings
import platform.UIKit.UIViewController

object AppLauncher {
    fun create(): UIViewController = ComposeUIViewController {
        App()
    }
}

fun isUserLoggedIn(): Boolean =
    Settings().getBoolean("is_logged_in", false)
