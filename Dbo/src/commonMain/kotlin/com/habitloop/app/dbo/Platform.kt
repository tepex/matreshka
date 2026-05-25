package com.habitloop.app.dbo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
