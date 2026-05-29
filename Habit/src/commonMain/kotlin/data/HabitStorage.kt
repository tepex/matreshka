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
    private val settings: Settings? = try {
        Settings()
    } catch (e: Exception) {
        //createInMemorySettings()
        null
    }

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

    fun isEmpty() = settings == null

    fun getRawHabits(): List<Habit> {
        val jsonString = settings?.getString(KEY_HABITS, "") ?: ""
        //if (jsonString.isEmpty()) return emptyList()
        if (jsonString.isEmpty()) return defaultHabits
        return json.decodeFromString<List<Habit>>(jsonString)
    }

    fun saveRawHabits(list: List<Habit>) {
        val jsonString = json.encodeToString(list)
        settings?.putString(KEY_HABITS, jsonString)
    }

    fun getHistoryMap(): Map<String, Boolean> {
        val jsonString = settings?.getString(KEY_HISTORY, "") ?: ""
        if (jsonString.isEmpty()) return emptyMap()
        return json.decodeFromString<Map<String, Boolean>>(jsonString)
    }

    fun saveHistoryMap(map: Map<String, Boolean>) {
        val jsonString = json.encodeToString(map)
        settings?.putString(KEY_HISTORY, jsonString)
    }

    private fun createInMemorySettings(): Settings {
        val map = mutableMapOf<String, Any>()
        return object : Settings {
            override val keys: Set<String> get() = map.keys
            override val size: Int get() = map.size

            override fun clear() = map.clear()
            override fun remove(key: String) {
                map.remove(key)
            }

            override fun hasKey(key: String): Boolean = map.containsKey(key)

            override fun putString(key: String, value: String) {
                map[key] = value
            }

            override fun getString(key: String, defaultValue: String): String = map[key] as? String ?: defaultValue
            override fun getStringOrNull(key: String): String? = map[key] as? String

            override fun putInt(key: String, value: Int) {
                map[key] = value
            }

            override fun getInt(key: String, defaultValue: Int): Int = map[key] as? Int ?: defaultValue
            override fun getIntOrNull(key: String): Int? = map[key] as? Int

            override fun putLong(key: String, value: Long) {
                map[key] = value
            }

            override fun getLong(key: String, defaultValue: Long): Long = map[key] as? Long ?: defaultValue
            override fun getLongOrNull(key: String): Long? = map[key] as? Long

            override fun putFloat(key: String, value: Float) {
                map[key] = value
            }

            override fun getFloat(key: String, defaultValue: Float): Float = map[key] as? Float ?: defaultValue
            override fun getFloatOrNull(key: String): Float? = map[key] as? Float

            override fun putDouble(key: String, value: Double) {
                map[key] = value
            }

            override fun getDouble(key: String, defaultValue: Double): Double = map[key] as? Double ?: defaultValue
            override fun getDoubleOrNull(key: String): Double? = map[key] as? Double

            override fun putBoolean(key: String, value: Boolean) {
                map[key] = value
            }

            override fun getBoolean(key: String, defaultValue: Boolean): Boolean = map[key] as? Boolean ?: defaultValue
            override fun getBooleanOrNull(key: String): Boolean? = map[key] as? Boolean
        }
    }
}
