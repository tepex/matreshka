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
    val typeColor: TypeColor,
    @Serializable(with = LocalDateComponentSerializer::class)
    val createdDate: LocalDate,
    val isCompleted: Boolean
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
        RUN, READ, MEDITATE, DRINK, TENNIS, BASKETBALL, SOCCER

    }

    @Serializable
    enum class TypeColor {
        COLOR1,
        COLOR2,
        COLOR3,
        COLOR4,
        COLOR5,
        COLOR6,
        COLOR7

    }

}
