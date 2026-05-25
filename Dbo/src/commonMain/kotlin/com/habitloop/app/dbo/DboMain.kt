package com.habitloop.app.dbo

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun DboViewController(): UIViewController = ComposeUIViewController {
    App()
}
