package pl.alan.services.controller

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.alan.services.model.Habit
import pl.alan.services.repository.DefaultHabitDbRepository

@RestController
@RequestMapping("/habits")
class HabitController {

    @Autowired
    lateinit var repository: DefaultHabitDbRepository
/*

*/

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody h: Habit) {
        transaction {
            repository.create(h)
        }
    }

    @GetMapping
    fun findAll(): List<Habit> =
            transaction {
                repository.findAll()
            }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): List<Habit> =
            transaction {
                repository.findById(id)
            }

    @DeleteMapping
    fun deleteAll() {
        transaction {
            repository.deleteAll()
        }
    }

    @DeleteMapping("/id")
    fun deleteById(@PathVariable id: Int ){
        transaction {
            repository.deleteById(id)
        }
    }

    @PutMapping ("/id")
    fun update (@PathVariable id: Int, @RequestBody habit: Habit): Habit =
            transaction {
                repository.update(id, habit)
            }
/*
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): Habit? = repository.findById(id)

    @PostMapping
    fun add(@RequestBody habit: Habit): Habit = repository.save(habit)

    @PutMapping
    fun update(@RequestBody habit: Habit): Habit = repository.update(habit)

    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: Int): Boolean = repository.removeById(id)

*/

}
