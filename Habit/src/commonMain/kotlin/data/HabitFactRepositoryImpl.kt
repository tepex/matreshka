package com.habitloop.app.habit.data

import com.habitloop.app.habit.domain.HabitFactRepository
import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HabitFactRepositoryImpl : HabitFactRepository {

    // json: date -> List<HabitFact>
    private var storage: String = ""

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override fun getHabitFacts(day: LocalDate): List<HabitFact> =
        decode()[day] ?: emptyList()

    override fun add(day: LocalDate, fact: HabitFact): Result<HabitFact> =
        decode().toMutableMap().let { map ->
            (map[day]?.toMutableList() ?: mutableListOf()).let { facts ->
                facts.add(fact)
                map[day] = facts
                storage = json.encodeToString(map)
                Result.success(fact)
            }
        }

    override fun addAll(day: LocalDate, facts: List<HabitFact>): Result<List<HabitFact>> =
        decode().toMutableMap().let { map ->
            (map[day]?.toMutableList() ?: mutableListOf()).let { existing ->
                existing += facts
                map[day] = existing
                storage = json.encodeToString(map)
                Result.success(facts)
            }
        }

    override fun update(day: LocalDate, i: Int, fact: HabitFact): Result<HabitFact> =
        decode().toMutableMap().let { map ->
            map[day]?.toMutableList()?.let { facts ->
                facts[i] = fact
                map[day] = facts
                storage = json.encodeToString(map)
                println("set completed for day[$i]: $day, $storage")
                Result.success(fact)
            } ?: Result.failure(NoSuchElementException("HabitFact $i not found for date: $day"))
        }

    override fun getStartDate(habitId: Habit.Id): Result<LocalDate> = runCatching {
        decode().filter { (_, facts) -> facts.any { it.habitId.value == habitId.value } }.keys.minOrNull()
            ?: throw NoSuchElementException("habit.id: $habitId not found")
    }

    private fun decode(): Map<LocalDate, List<HabitFact>> =
        storage.takeIf { it.isNotBlank() }?.let { json.decodeFromString<Map<LocalDate, List<HabitFact>>>(storage) }
            ?: emptyMap()
}
