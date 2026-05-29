package com.habitloop.app.habit.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.LocalDateComponentSerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline


@Serializable
data class Habit(
    val id: Id,
    val name: Name,
    val description: Description,
    val type: Type,
    @Serializable(with = LocalDateComponentSerializer::class)
    val createdDate: LocalDate
) {

    @Serializable
    @JvmInline
    value class Id(val value: Int)

    @Serializable
    @JvmInline
    value class Name(val value: String)

    @Serializable
    @JvmInline
    value class Description(val value: String)

    @Serializable
    enum class Type {
        RUN, READ, MEDITATE, DRINK, EXERCISE, OTHER
    }
}
