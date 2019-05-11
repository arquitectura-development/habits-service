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
import pl.alan.services.repository.HabitRepository

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
        Database.connect("jdbc:mysql://127.0.0.1:3306/habits", driver = "com.mysql.jdbc.Driver")
        var habit = Habit(1, 1, "Do Clean Code", habitType = 1, difficulty = 2 , score = 2.0, color = 1)
        habit = transaction {
            repository.create(habit)
        }
        Assert.assertNotNull(habit)
        Assert.assertNotNull(habit.name)
        Assert.assertEquals(1, habit.userID)
    }

    @Test
    fun test2UpdateHabit() {
        var habit = Habit(1, 2, "Do Clean Code", habitType = 1, difficulty = 3 , score = 3.0, color = 2)
        habit = transaction {
            repository.update(1, 1, habit)
        }
        Assert.assertNotNull(habit)
        Assert.assertEquals(2,  habit.userID)
    }




    @Test
    fun test3GetHabitByID() {

        var lista  = listOf<Habit>()

        lista = transaction {
            repository.findByUserId(2, 1)
        }
        Assert.assertEquals(1 , lista.size)

    }

    @Test
    fun test4DeleteHabit() {
        var habit = transaction {
            repository.deleteById(1, 1)
        }
        Assert.assertEquals(0 , habit)
    }

    @Test
    fun test5DeleteHabits() {
        val habit = transaction {
            repository.deleteAll()
        }
        Assert.assertEquals(0, 0)
    }

    @Test
    fun test6GetHabits() {
        var habit = Habit(1, 1, "Do Clean Code", habitType = 1, difficulty = 2 , score = 2.0, color = 1)
        habit = transaction {
            repository.create(habit)
        }
        var habit2 = Habit(2, 2, "Do Clean Code 2", habitType = 1, difficulty = 2 , score = 2.0, color = 1)
        habit2 = transaction {
            repository.create(habit2)
        }

        var lista  = listOf<Habit>()

        lista = transaction {
            repository.findAll(1)
        }
        Assert.assertEquals(2 , lista.size)

    }



    @Test
    fun test7GetHabitColor() {
        var habit = Habit(1, 1, "Do Clean Code", habitType = 1, difficulty = 2 , score = 30.0, color = 1)
        habit = transaction {
            repository.create(habit)
        }
        var color = repository.getHabitColor(habit)
        Assert.assertEquals(3, color)
    }

    @Test
    fun test8UpdateHabitScore() {
        var habit = Habit(1, 1, "Do Tests", habitType = 1, difficulty = 5 , score = 30.0, color = 4)
        habit = transaction {
            repository.create(habit)
        }
        repository.updateHabitScore(habit, 1)
        Assert.assertEquals(32.5, habit.score, 2.5)

        var habit2 = Habit(2, 1, "Get Drunk", habitType = 2, difficulty = 3 , score = 8.0, color = 4)
        habit2 = transaction {
            repository.create(habit2)
        }
        repository.updateHabitScore(habit2, 1)
        Assert.assertEquals(2.0, habit2.score, 6.0)

    }


}