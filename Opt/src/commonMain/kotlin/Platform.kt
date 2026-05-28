package com.habitloop.app.opt

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
