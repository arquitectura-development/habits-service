package pl.alan.services

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringRunner
import pl.alan.services.model.Habit
import pl.alan.services.repository.DefaultHabitDbRepository

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HabitControllerTests {

    @Autowired
    lateinit var template: TestRestTemplate
    @Autowired
    lateinit var repository: DefaultHabitDbRepository


    @Test
    fun test1AddHabit() {
        Database.connect("jdbc:mysql://127.0.0.1:3306/test", driver = "com.mysql.jdbc.Driver")
        var habit = Habit(1, 1, "Do Clean Code", habitType = 1, difficulty = 2, score = 2.0, color = 1)
        habit = transaction {
            repository.createTable()
            repository.create(habit)
        }
        Assert.assertNotNull(habit)
        Assert.assertNotNull(habit.name)
        Assert.assertEquals(1, habit.userID)
    }

    @Test
    fun test2UpdateHabit() {
        var habit = Habit(1, 1, "Do Clean Code", habitType = 1, difficulty = 3, score = 3.0, color = 2)
        habit = transaction {
            repository.update(1, 1, habit)
        }
        Assert.assertNotNull(habit)
        Assert.assertEquals(3, habit.difficulty)
    }

    @Test
    fun test3GetHabitByID() {
        var lista = listOf<Habit>()

        lista = transaction {
            repository.findByUserHabitId(1, 1)
        }
        Assert.assertEquals(1, lista.size)

        lista = transaction {
            repository.findByUserId(1)
        }
        Assert.assertEquals(1, lista.size)

    }

    @Test
    fun test4DeleteHabit() {
        var success: Int = transaction {
            repository.deleteById(1, 1)
        }
        Assert.assertEquals(1, success)
    }


    @Test
    fun test5GetHabits() {
        var habit2 = Habit(2, 1, "Do Clean Code 2", habitType = 1, difficulty = 2 , score = 2.0, color = 1)
        habit2 = transaction {
            repository.create(habit2)
        }
        var habit3 = Habit(3, 2, "Do Clean Code 3", habitType = 2, difficulty = 2 , score = 2.0, color = 1)
        habit3 = transaction {
            repository.create(habit3)
        }

        var lista = listOf<Habit>()

        lista = transaction {
            repository.findAll(0)
        }
        Assert.assertEquals(2, lista.size)

    }

    @Test
    fun test6GetHabitColor() {
        var habit4 = Habit(4, 1, "Do Clean Code 4", habitType = 3, difficulty = 2, score = 30.0, color = 1)
        habit4 = transaction {
            repository.create(habit4)
        }
        var color = repository.getHabitColor(habit4)
        Assert.assertEquals(3, color)
    }

    @Test
    fun test7UpdateHabitScoreType1() {
        var habit2 = Habit(2, 1, "Do Clean Code 2", habitType = 1, difficulty = 2, score = 2.0, color = 1)
        habit2 = transaction {
            repository.updateHabitScore(1, 2, true, habit2)
        }
        Assert.assertEquals(5.0, habit2.score, 3.0)
    }

    @Test
    fun test8UpdateHabitScoreType2() {
        var habit3 = Habit(3, 2, "Do Clean Code 3", habitType = 2, difficulty = 2, score = 2.0, color = 1)
        habit3 = transaction {
            repository.updateHabitScore(2, 3, true, habit3)
        }
        Assert.assertEquals(0.0, habit3.score, 2.5)


    }

    @Test
    fun test9UpdateHabitScoreType3() {
        var habit4 = Habit(4, 1, "Do Clean Code 4", habitType = 3, difficulty = 2, score = 30.0, color = 1)
        habit4 = transaction {
            repository.updateHabitScore(1, 4, true, habit4)
        }
        Assert.assertEquals(32.5, habit4.score, 2.5)

        habit4 = transaction {
            repository.updateHabitScore(1, 4, false, habit4)
        }
        Assert.assertEquals(30.0, habit4.score, 2.0)

    }

    @Test
    fun test9ZDeleteHabits() {
        var success: Int = transaction {
            repository.deleteAll()
        }
        Assert.assertEquals(3, success)
    }


}