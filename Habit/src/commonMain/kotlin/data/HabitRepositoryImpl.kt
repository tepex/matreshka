package com.habitloop.app.habit.data

import com.habitloop.app.habit.domain.HabitRepository
import com.habitloop.app.habit.domain.model.Habit
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HabitRepositoryImpl(/*private val storage: HabitStorage*/) : HabitRepository {

    // json: Set<Habit>
    private var storage = ""
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /*
    init {
        val jsonString = json.encodeToString(predefinedHabits)
        storage = jsonString
        println("[HabitRepository] Init: $storage")
    }*/

    /*
    override fun getHabits(): List<Habit> =
        //storage.getRawHabits().ifEmpty { defaultHabits }
        storage.getRawHabits()

    override fun addHabit(dto: HabitDto): Habit {
        val currentHabits = storage.getRawHabits().toMutableList()

        // Генерируем следующий ID на основе распакованного int-значения
        val nextIdValue = (currentHabits.maxOfOrNull { it.id.value } ?: 0) + 1

        val newHabit = Habit(
            id = Habit.Id(nextIdValue),
            name = dto.name,
            description = dto.description,
            type = dto.type,
            typeColor = dto.typeColor,
            createdDate = dto.createdDate,
            false
        )

        currentHabits.add(newHabit)
        storage.saveRawHabits(currentHabits)
        return newHabit
    }

    override fun isHabitCompleted(habitId: Habit.Id, date: LocalDate): Boolean {
        // Формируем уникальный ключ
        val historyKey = "${date}_${habitId.value}"
        return storage.getHistoryMap()[historyKey] ?: false
    }

    override fun toggleHabitCompletion(habitId: Habit.Id, date: LocalDate) {
        val historyKey = "${date}_${habitId.value}"
        val currentHistory = storage.getHistoryMap().toMutableMap()

        val currentStatus = currentHistory[historyKey] ?: false
        currentHistory[historyKey] = !currentStatus

        storage.saveHistoryMap(currentHistory)
    }*/

    override fun create(habit: Habit): Result<Habit> =
        (storage.takeIf { it.isNotBlank() }
            ?.let { json.decodeFromString<Set<Habit>>(it).toMutableSet() } ?: mutableSetOf())
            .let { set ->
                set += habit
                storage = json.encodeToString(set)
                println("[HabitRepository] added new habit $habit")
                /*
                println("[HabitRepository] saved habits:")
                println(storage)*/
                Result.success(habit)
            }

    override fun getHabitsByDayOfWeek(day: DayOfWeek): Set<Habit> =
        storage.takeIf { it.isNotBlank() }?.let { json.decodeFromString<Set<Habit>>(it) }
            ?.filter { it.data.weekly.value[day.ordinal] }
            ?.toSet()
            ?.also { println("[HabitRepository] get($day): $it") }
            ?: emptySet()

    override fun getHabit(id: Habit.Id): Result<Habit> =
        storage.takeIf { it.isNotBlank() }?.let {
            json.decodeFromString<Set<Habit>>(it)
                .firstOrNull() { it.id == id } ?.let { Result.success(it) }
        } ?: Result.failure(NoSuchElementException("Habit id: $id not found"))
}

val predefinedHabits = setOf(
    Habit.create("test", Habit.Data.IconType.ICON1, Habit.Data.ColorType.COLOR1, setOf(1,6)),
    Habit.create("test 3", Habit.Data.IconType.ICON3, Habit.Data.ColorType.COLOR3, setOf(2,6)),

    Habit.create("every day 1", Habit.Data.IconType.ICON4, Habit.Data.ColorType.COLOR4, setOf(0,1,2,3,4,5,6)),
    Habit.create("every day 2", Habit.Data.IconType.ICON5, Habit.Data.ColorType.COLOR5, setOf(0,1,2,3,4,5,6))
)
