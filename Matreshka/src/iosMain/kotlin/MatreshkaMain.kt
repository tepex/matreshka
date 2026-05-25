package com.habitloop.app.matreshka

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MatreshkaViewController(): UIViewController = ComposeUIViewController {
    App()
}
