package com.habitloop.app.matreshka

import androidx.compose.ui.window.ComposeUIViewController
import com.habitloop.app.matreshka.ui.App
import com.habitloop.app.matreshka.ui.LoginScreen
import com.russhwolf.settings.Settings
import platform.UIKit.UIViewController

fun MatreshkaViewController(onSuccess: () -> Unit): UIViewController = ComposeUIViewController {
    LoginScreen(onSuccess = onSuccess)
}
fun isUserLoggedIn(): Boolean =
    Settings().getBoolean("is_logged_in", false)
