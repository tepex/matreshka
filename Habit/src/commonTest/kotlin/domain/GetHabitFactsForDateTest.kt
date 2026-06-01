package com.habitloop.app.habit.domain

import com.habitloop.app.habit.domain.model.Habit
import com.habitloop.app.habit.domain.model.HabitFact
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

class GetHabitFactsForDateTest : FunSpec({

    // Фиксированные даты для детерминированности тестов
    val may30 = LocalDate(2026, 5, 30)
    val may29 = LocalDate(2026, 5, 29)
    val may31 = LocalDate(2026, 5, 31)

    // Тестовые ID привычек
    val habitId1 = Habit.Id(1)
    val habitId2 = Habit.Id(2)

    // Базовые данные для привычек
    val dummyData1 = Habit.Data(
        name = Habit.Data.Name("Бег"),
        icon = Habit.Data.IconType.ICON1,
        color = Habit.Data.ColorType.COLOR1,
        weekly = Habit.Data.Weekly(),
        freeze = false
    )
    val dummyData2 = dummyData1.copy(name = Habit.Data.Name("Чтение"))

    // Тестовые сущности Habit
    val habit1 = Habit(id = habitId1, data = dummyData1)
    val habit2 = Habit(id = habitId2, data = dummyData2)

    // 1. Тест для ПРОШЛОЙ даты
    test("getHabitFactsForDate для ПРОШЛОЙ даты должен возвращать только сохраненные факты и не дополнять новыми") {
        // На прошлый день запланировано 2 привычки, но выполнен был только один факт
        val habitsStub = setOf(habit1, habit2)
        val savedFacts = listOf(HabitFact.create(habitId1).setCompletion(isCompleted = true))

        val habitRepo = FakeHabitRepository(habitsStub)
        val factRepo = FakeHabitFactRepository(savedFacts)

        val habitFacts = getHabitFactsForDate(may29, may30, factRepo, habitRepo)

        // Должен вернуться ровно один агрегат (сохраненный факт), без догенерации второй привычки
        habitFacts shouldHaveSize 1
        habitFacts.first().fact.habitId shouldBe habitId1
        habitFacts.first().fact.isCompleted shouldBe true
        habitFacts.first().habitData.name.value shouldBe "Бег"
    }

    // 2. Тест для ТЕКУЩЕЙ даты (30/V)
    test("getHabitFactsForDate для СЕГОДНЯШНЕЙ даты должен возвращать сохраненные факты и ДОПОЛНЯТЬ список недостающими привычками") {
        // На сегодня запланировано 2 привычки, в базе сохранен факт только для первой
        val habitsStub = setOf(habit1, habit2)
        val savedFacts = listOf(HabitFact.create(habitId1).setCompletion(isCompleted = true))

        val habitRepo = FakeHabitRepository(habitsStub)
        val factRepo = FakeHabitFactRepository(savedFacts)

        val habitFacts = getHabitFactsForDate(may30, may30, factRepo, habitRepo)

        // Ожидаем 2 элемента: один из базы (выполнен), второй сгенерирован автоматически (не выполнен)
        habitFacts shouldHaveSize 2

        val firstAggregate = habitFacts.first { it.fact.habitId == habitId1 }
        firstAggregate.fact.isCompleted shouldBe true
        firstAggregate.habitData.name.value shouldBe "Бег"

        val secondAggregate = habitFacts.first { it.fact.habitId == habitId2 }
        secondAggregate.fact.isCompleted shouldBe false
        secondAggregate.habitData.name.value shouldBe "Чтение"
    }

    // 3. Тест для БУДУЩЕЙ даты
    test("getHabitFactsForDate для БУДУЩЕЙ даты должен возвращать пустые факты для всех запланированных привычек") {
        // На будущее запланировано 2 привычки. В репозитории фактов пусто.
        val habitsStub = setOf(habit1, habit2)

        val habitRepo = FakeHabitRepository(habitsStub)
        val factRepo = FakeHabitFactRepository(emptyList()) // В будущем фактов в БД быть не может

        val habitFacts = getHabitFactsForDate(may31, may30, factRepo, habitRepo)

        // Должны вернуться дефолтные агрегаты для обеих привычек со статусом false
        habitFacts shouldHaveSize 2
        habitFacts.all { !it.fact.isCompleted } shouldBe true
        habitFacts.map { it.fact.habitId }.toSet() shouldBe setOf(habitId1, habitId2)
    }

    // 4. Тест граничного случая: удаленная привычка
    test("getHabitFactsForDate должен игнорировать сохраненные факты, если сама привычка была удалена из репозитория") {
        // В базе фактов лежит старая отметка, но в репозитории привычек этой привычки больше нет
        val habitsStub = emptySet<Habit>()
        val savedFacts = listOf(HabitFact.create(habitId1))

        val habitRepo = FakeHabitRepository(habitsStub)
        val factRepo = FakeHabitFactRepository(savedFacts)

        val habitFacts = getHabitFactsForDate(may30, may30, factRepo, habitRepo)

        // Результат должен быть пустым (автоматическое очищение от архивных/удаленных фактов)
        habitFacts shouldHaveSize 0
    }
})

// --- ЛЕГКОВЕСНЫЕ FAKE-РЕПОЗИТОРИИ ДЛЯ ИЗОЛИРОВАННОГО ТЕСТИРОВАНИЯ ---

private class FakeHabitRepository(private val stubHabits: Set<Habit>) : HabitRepository {
    override fun getHabitsByDayOfWeek(day: DayOfWeek): Set<Habit> {
        // Для простоты тестов возвращаем фиксированный набор независимо от дня недели
        return stubHabits
    }
}

private class FakeHabitFactRepository(private val stubFacts: List<HabitFact>) : HabitFactRepository {
    override fun getHabitFacts(day: LocalDate): List<HabitFact> {
        return stubFacts
    }

    override fun add(
        day: LocalDate,
        fact: HabitFact
    ): Result<HabitFact> {
        TODO("Not yet implemented")
    }

    override fun update(day: LocalDate, i: Int, fact: HabitFact): Result<HabitFact> {
        TODO("Not yet implemented")
    }
}

