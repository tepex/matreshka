package com.habitloop.app.matreshka

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
