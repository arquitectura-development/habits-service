package pl.alan.services.controller

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.alan.services.model.Habit
import pl.alan.services.repository.DefaultHabitDbRepository

@RestController
@RequestMapping //("/habits")
class HabitController {

    @Autowired
    lateinit var repository: DefaultHabitDbRepository
/*

*/

    @PostMapping("/users/habits")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody body: Habit, @RequestParam userId: Int) {
        transaction {
            repository.create(body)
        }

    }

    @GetMapping("/admin/habits")
    fun findAll(@RequestParam userId: Int): List<Habit> =
            transaction {
                repository.findAll()
            }

    @GetMapping("/users/habits")
    fun findByUserId(@RequestParam userId: Int): List<Habit> =
            transaction {
                repository.findByUserId(userId)
            }

    @DeleteMapping("/admin/habits")
    fun deleteAll(@RequestParam userId: Int) {
        transaction {
            repository.deleteAll()
        }
    }

    @DeleteMapping("/users/habits")
    fun deleteById(@RequestParam userId: Int){
        transaction {
            repository.deleteById(userId)
        }
    }

    @PutMapping("/users/habits")
    fun update (@RequestParam id: Int, @RequestParam userId: Int, @RequestBody habit: Habit): Habit =
            transaction {
                repository.update(id, habit)
            }

}
