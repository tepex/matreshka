package com.habitloop.app.matreshka

import androidx.compose.ui.window.ComposeUIViewController
import com.habitloop.app.matreshka.ui.App
import platform.UIKit.UIViewController

fun MatreshkaViewController(): UIViewController = ComposeUIViewController {
    App()
}
