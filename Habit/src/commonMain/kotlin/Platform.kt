package com.habitloop.app.habit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
