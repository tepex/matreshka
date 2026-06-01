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

    override fun update(day: LocalDate, i: Int, fact: HabitFact): Result<HabitFact> =
        decode().toMutableMap().let { map ->
            map[day]?.toMutableList()?.let { facts ->
                facts[i] = fact
                map[day] = facts
                storage = json.encodeToString(map)
                println("set completed for day[$i]: $day, $storage")
                Result.success(fact)
            } ?: Result.failure(RuntimeException("HabitFact $i not found for date: $day"))
        }

        /*
        getHabitFacts(day).toMutableList()[i]
        println("set completed: $isCompleted for $day[$i]")
        return Result.success(HabitFact.create(Habit.Id(-1)))*/

    private fun decode(): Map<LocalDate, List<HabitFact>> =
        storage.takeIf { it.isNotBlank() }?.let { json.decodeFromString<Map<LocalDate, List<HabitFact>>>(storage) }
            ?: emptyMap()
}
