package com.habitloop.app.habit.domain

import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
object HabitIdGenerator {

    private val counter = AtomicInt(0)

    fun nextId(): Int =
        counter.addAndFetch(1)
}
