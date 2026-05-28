package com.habitloop.app.opt

import androidx.compose.ui.window.ComposeUIViewController
import com.habitloop.app.opt.ui.App
import platform.UIKit.UIViewController

fun DboViewController(): UIViewController = ComposeUIViewController {
    App()
}
