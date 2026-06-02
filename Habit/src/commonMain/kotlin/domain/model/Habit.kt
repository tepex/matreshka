package com.habitloop.app.habit.domain.model

import com.habitloop.app.habit.data.HabitIdGenerator
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Сущность (DDD Entity) «Привычка»
 *
 * */
@Serializable
class Habit(
    val id: Id,
    var data: Data
) {

    override fun toString(): String =
        "[id: $id, data: $data]"

    @JvmInline
    @Serializable
    value class Id(val value: Int = HabitIdGenerator.nextId()) {
        override fun toString(): String =
            value.toString()

        companion object {
            val INVALID = Id(-1)
        }

    }

    @Serializable
    data class Data(
        val name: Name,
        val icon: IconType,
        val color: ColorType,
        val weekly: Weekly,
        val freeze: Boolean
    ) {

        @Serializable
        @JvmInline
        value class Name(val value: String) {
            init {
                require(value.isNotBlank() && value.length <= 40)
            }
        }

        @Serializable
        enum class IconType {
            //RUN, READ, MEDITATE, DRINK, TENNIS, BASKETBALL, SOCCER
            ICON1, ICON2, ICON3, ICON4, ICON5, ICON6, ICON7,
        }

        @Serializable
        enum class ColorType(val color: Long) {
            COLOR1(0xFF0066CC),
            COLOR2(0xFFE53935),
            COLOR3(0xFFFFB300),
            COLOR4(0xFF4CAF50),
            COLOR5(0xFF1C1C1E),
            COLOR6(0xFF9C27B0),
            COLOR7(0xFFC7A167)
        }

        @Serializable
        data class Weekly(val value: BooleanArray = BooleanArray(7) { false }) {
            override fun equals(other: Any?): Boolean {
                if (other !is Weekly) return false
                return this.value.contentEquals(other.value)
            }

            override fun hashCode(): Int {
                return this.value.contentHashCode()
            }

            override fun toString(): String =
                value.joinToString(prefix = "[", postfix = "]")

        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Habit
        return id == other.id
    }

    override fun hashCode(): Int =
        id.hashCode()

    companion object {
        fun create(
            name: String,
            icon: Data.IconType,
            color: Data.ColorType,
            weekIndicies: Set<Int>,
        ): Habit =
            Data(
                Data.Name(name),
                icon,
                color,
                Data.Weekly(BooleanArray(7) { i -> i in weekIndicies }),
                false
            ).let { Habit(Habit.Id(), it) }
    }
}
