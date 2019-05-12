package pl.alan.services.controller

import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.alan.services.model.Habit
import pl.alan.services.repository.DefaultHabitDbRepository

@RestController
@RequestMapping //("/habits")
class HabitController {

    @Autowired
    lateinit var repository: DefaultHabitDbRepository

    /*

*/

    @GetMapping()
    @ApiResponses(
            ApiResponse(code = 200, message = "The service is up")
    )
    fun up(): String {
        return "Service is up"
    }

    @GetMapping("/admin/habits")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 401, message = "Incorrect or empty userId"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun findAll(@RequestParam userId: Int): List<Habit> =
            transaction {
                if (userId == 0)
                    repository.findAll(userId)
                else throw  ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Incorrect or empty userId");
            }


    @GetMapping("/users/habits")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 401, message = "Incorrect or empty userId"),
            ApiResponse(code = 404, message = "Habit or user not found"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun findByUserId(@RequestParam userId: Int): List<Habit> {
        var lista = listOf<Habit>()
        transaction {
            if (userId != null) {
                lista = repository.findByUserId(userId)
                if (lista.isEmpty()){
                    throw  ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Habit or user not found");
                }
            }else throw  ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Incorrect or empty userId");
        }
        return lista
    }

    @PostMapping("/users/habits")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful habit creation"),
            ApiResponse(code = 400, message = "Incorrect json"),
            ApiResponse(code = 401, message = "Incorrect or empty userId"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun create(@RequestBody body: Habit, @RequestParam userId: Int) {
        var habito: Habit = body
        transaction {
            if (body != null) {
                if (userId != null) {
                    habito = repository.create(body)

                } else throw  ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Incorrect or empty userId");
            } else throw  ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect json");
        }

    }

    @GetMapping("/users/habits/{habitId}")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 401, message = "Incorrect or empty userId"),
            ApiResponse(code = 404, message = "Habit or user not found"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun findByUserId(@RequestParam userId: Int, @PathVariable habitId: Int): List<Habit> {
        var lista = listOf<Habit>()
        transaction{
            if (userId != null) {
                lista = repository.findByUserId(userId, habitId)
                if (lista.isEmpty()){
                    throw  ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Habit or user not found");
                }
            }
            else throw  ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Incorrect or empty userId");
        }
        return lista
    }


    @DeleteMapping("/users/habits/{habitId}")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 401, message = "Incorrect or empty userId"),
            ApiResponse(code = 404, message = "Habit or user not found"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun deleteById(@RequestParam userId: Int, @PathVariable habitId: Int) {
        var num: Int = 1
        transaction {
            if (userId != null) {
                num = repository.deleteById(userId, habitId)
                if (num == 0) {
                    throw  ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Habit or user not found")
                }
            }else throw  ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Incorrect or empty userId");
        }
    }


    @PutMapping("/users/habits/{habitId}")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 400, message = "Incorrect json"),
            ApiResponse(code = 401, message = "Incorrect or empty userId"),
            ApiResponse(code = 404, message = "Habit or user not found"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun update(@PathVariable habitId: Int, @RequestParam userId: Int, @RequestBody body: Habit): Habit {
        var habito : Habit = body
        var lista = listOf<Habit>()

        transaction {
                if (body != null) {
                    if (userId != null) {
                        lista = repository.findByUserId(userId, habitId)
                        if (lista.isEmpty()){
                            throw  ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Habit or user not found");
                        }
                        else{
                            habito = repository.updateHabitScore(userId, habitId, body)
                            //repository.update(userId, habitId, body)

                        }
                    }
                    else throw  ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Incorrect userID");
                }
                else throw  ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Incorrect json");

            }
        return habito
    }
}


