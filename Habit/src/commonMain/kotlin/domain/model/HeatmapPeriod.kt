package com.habitloop.app.habit.domain.model

enum class HeatmapPeriod(val rowsCount: Int) {
    WEEK1(rowsCount = 1),
    WEEK2(rowsCount = 2),
    WEEK4(rowsCount = 4)
}
