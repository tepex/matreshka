package com.habitloop.app.habit.data

import com.russhwolf.settings.Settings
import com.habitloop.app.habit.domain.Habit
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class HabitStorage {
    private val settings = Settings()

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    companion object {
        private val KEY_HABITS = "saved_habits_list"
        private val KEY_HISTORY = "habits_execution_history"
    }

    init {
        // Если база пуста, наполняем её стартовыми привычками с текущей датой создания
        /*
        if (settings.getString(KEY_HABITS, "").isEmpty()) {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val defaultList = listOf(
                Habit(Id(1), Name("Утренняя пробежка"), Description("11 дней"), Habit.Type.RUN, today),
                Habit(Id(2), Name("Чтение 30 мин"), Description("5 дней"), Habit.Type.READ, today),
                Habit(Id(3), Name("Медитация"), Description("31 день подряд"), Habit.Type.MEDITATE, today),
                Habit(Id(4), Name("Пить воду 2л"), Description("0 дней"), Habit.Type.DRINK, today)
            )
            saveRawHabits(defaultList)
        }*/
    }

    fun getRawHabits(): List<Habit> {
        val jsonString = settings.getString(KEY_HABITS, "")
        if (jsonString.isEmpty()) return emptyList()
        return json.decodeFromString<List<Habit>>(jsonString)
    }

    fun saveRawHabits(list: List<Habit>) {
        val jsonString = json.encodeToString(list)
        settings.putString(KEY_HABITS, jsonString)
    }

    fun getHistoryMap(): Map<String, Boolean> {
        val jsonString = settings.getString(KEY_HISTORY, "")
        if (jsonString.isEmpty()) return emptyMap()
        return json.decodeFromString<Map<String, Boolean>>(jsonString)
    }

    fun saveHistoryMap(map: Map<String, Boolean>) {
        val jsonString = json.encodeToString(map)
        settings.putString(KEY_HISTORY, jsonString)
    }
}
