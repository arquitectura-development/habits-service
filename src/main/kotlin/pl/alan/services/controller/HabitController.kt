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
import org.springframework.http.ResponseEntity



@RestController
@RequestMapping //("/habits")
class HabitController {

    @Autowired
    lateinit var repository: DefaultHabitDbRepository

    @GetMapping()
    @ApiResponses(
            ApiResponse(code = 200, message = "The service is up")
    )
    fun up(): String {
        return "Service is up"
    }

    @GetMapping("/admin/habits")
    @ApiResponses(
            ApiResponse(code = 200, message = SUCCESSRESPONSE),
            ApiResponse(code = 401, message = UNAUTHORIZEDRESPONSE),
            ApiResponse(code = 500, message = SERVERERRORRESPONSE)
    )
    fun findAll(@RequestParam userId: Int): List<Habit> =
            transaction {
                if (userId == 0)
                    repository.findAll(userId)
                else throw  ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, UNAUTHORIZEDRESPONSE)
            }

    @GetMapping("/users/habits")
    @ApiResponses(
            ApiResponse(code = 200, message = SUCCESSRESPONSE),
            ApiResponse(code = 401, message = UNAUTHORIZEDRESPONSE),
            ApiResponse(code = 404, message = NOTFOUNDRESPONSE),
            ApiResponse(code = 500, message = SERVERERRORRESPONSE)
    )
    fun findByUserId(@RequestParam userId: Int): List<Habit> {
        var lista = listOf<Habit>()
        transaction {
            if (userId != null) {
                lista = repository.findByUserId(userId)
                if (lista.isEmpty()){
                    throw  ResponseStatusException(
                            HttpStatus.NOT_FOUND, NOTFOUNDRESPONSE)
                }
            }else throw  ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, UNAUTHORIZEDRESPONSE);
        }
        return lista
    }

    @PostMapping("/users/habits")
    @ApiResponses(
            ApiResponse(code = 201, message = "Successful habit creation"),
            ApiResponse(code = 400, message = BADREQUESTRESPONSE),
            ApiResponse(code = 401, message = UNAUTHORIZEDRESPONSE),
            ApiResponse(code = 500, message = SERVERERRORRESPONSE)
    )
    fun create(@RequestBody body: Habit, @RequestParam userId: Int): ResponseEntity<Any> {
        var habito: Habit = body
        transaction {
            if (body != null) {
                if (userId != null) {
                    habito = repository.create(body)

                } else throw  ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, UNAUTHORIZEDRESPONSE)
            } else throw  ResponseStatusException(
                    HttpStatus.BAD_REQUEST, BADREQUESTRESPONSE)
        }
        return ResponseEntity<Any>(HttpStatus.CREATED)
    }

    @GetMapping("/users/habits/{habitId}")
    @ApiResponses(
            ApiResponse(code = 200, message = SUCCESSRESPONSE),
            ApiResponse(code = 401, message = UNAUTHORIZEDRESPONSE),
            ApiResponse(code = 404, message = NOTFOUNDRESPONSE),
            ApiResponse(code = 500, message = SERVERERRORRESPONSE)
    )
    fun findByUserHabitId(@RequestParam userId: Int, @PathVariable habitId: Int): List<Habit> {
        var lista = listOf<Habit>()
        transaction{
            if (userId != null) {
                lista = repository.findByUserHabitId(userId, habitId)
                if (lista.isEmpty()) {
                    throw  ResponseStatusException(HttpStatus.NOT_FOUND, NOTFOUNDRESPONSE)
                }
            } else throw  ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, UNAUTHORIZEDRESPONSE)
        }
        return lista
    }


    @DeleteMapping("/users/habits/{habitId}")
    @ApiResponses(
            ApiResponse(code = 200, message = SUCCESSRESPONSE),
            ApiResponse(code = 401, message = UNAUTHORIZEDRESPONSE),
            ApiResponse(code = 404, message = NOTFOUNDRESPONSE),
            ApiResponse(code = 500, message = SERVERERRORRESPONSE)
    )
    fun deleteById(@RequestParam userId: Int, @PathVariable habitId: Int) {
        var num: Int = 1
        transaction {
            if (userId != null) {
                num = repository.deleteById(userId, habitId)
                if (num == 0) {
                    throw  ResponseStatusException(
                            HttpStatus.NOT_FOUND, NOTFOUNDRESPONSE)
                }
            } else throw  ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, UNAUTHORIZEDRESPONSE)
        }
    }


    @PutMapping("/users/habits/{habitId}")
    @ApiResponses(
            ApiResponse(code = 200, message = SUCCESSRESPONSE),
            ApiResponse(code = 400, message = BADREQUESTRESPONSE),
            ApiResponse(code = 401, message = UNAUTHORIZEDRESPONSE),
            ApiResponse(code = 404, message = NOTFOUNDRESPONSE),
            ApiResponse(code = 500, message = SERVERERRORRESPONSE)
    )
    fun update(@PathVariable habitId: Int, @RequestParam userId: Int, @RequestParam(required = false) positive: Boolean, @RequestParam(required = false) updateScore: Boolean, @RequestBody body: Habit): Habit {
        var habito : Habit = body
        var lista = listOf<Habit>()

        transaction {
            if (body != null) {
                if (userId != null) {
                    lista = repository.findByUserHabitId(userId, habitId)
                    if (lista.isEmpty()){
                        throw  ResponseStatusException(HttpStatus.NOT_FOUND, NOTFOUNDRESPONSE)
                    } else {
                        if (updateScore)
                            habito = repository.updateHabitScore(userId, habitId, positive, body)
                        else
                            habito = repository.update(userId, habitId, body)
                    }
                } else throw  ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect userID")
            } else throw  ResponseStatusException(HttpStatus.BAD_REQUEST, BADREQUESTRESPONSE)
        }
        return habito
    }

    companion object {
        const val SUCCESSRESPONSE = "Successful operation"
        const val UNAUTHORIZEDRESPONSE = "Incorrect or empty userId"
        const val NOTFOUNDRESPONSE = "Habit or user not found"
        const val SERVERERRORRESPONSE = "Server error"
        const val BADREQUESTRESPONSE = "Incorrect json"
    }
}


