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
    fun findByUserId(@PathVariable userId: Int): List<Habit> =
            transaction {
                repository.findByUserId(userId)
            }

    @DeleteMapping
    fun deleteAll() {
        transaction {
            repository.deleteAll()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Int ){
        transaction {
            repository.deleteById(id)
        }
    }

    @PutMapping ("/{id}")
    fun update (@PathVariable id: Int, @RequestBody habit: Habit): Habit =
            transaction {
                repository.update(id, habit)
            }

}
