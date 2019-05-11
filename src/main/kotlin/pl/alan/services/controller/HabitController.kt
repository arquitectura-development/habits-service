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

    @PostMapping("/users/habits")
    @ApiResponses(
            ApiResponse(code = 201, message = "Successful habit creation"),
            ApiResponse(code = 400, message = "Incorrect json"),
            ApiResponse(code = 401, message = "Incorrect userId"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun create(@RequestBody body: Habit, @RequestParam userId: Int) {
        transaction {
            if (body != null)
                repository.create(body)
            else throw  ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Incorrect json");
        }

    }

    @GetMapping("/admin/habits")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 401, message = "Incorrect userId"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun findAll(@RequestParam userId: Int): List<Habit> =
            transaction {
                if (userId == 0)
                    repository.findAll(userId)
                else throw  ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Incorrect userId");
            }

    @GetMapping("/users/habits/{id}")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 401, message = "Incorrect userId"),
            ApiResponse(code = 404, message = "Habit or user not found"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun findByUserId(@RequestParam userId: Int, @PathVariable (required=false)id: Int): List<Habit> =
            transaction {
                if (id != null)
                    repository.findByUserId(userId, id)
                else
                    repository.findByUserId(userId)
            }


    @DeleteMapping("/admin/habits")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 401, message = "Incorrect userId"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun deleteAll(@RequestParam userId: Int) =
        transaction {
            if (userId == 0)
                repository.deleteAll()
            else throw  ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Incorrect userId");

        }

    @DeleteMapping("/users/habits")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successful operation"),
            ApiResponse(code = 401, message = "Incorrect userId"),
            ApiResponse(code = 404, message = "Habit or user not found"),
            ApiResponse(code = 500, message = "Server error")
    )
    fun deleteById(@RequestParam userId: Int, @RequestParam id: Int) {
        transaction {
            repository.deleteById(userId, id)
        }


        @PutMapping("/users/habits")
        @ApiResponses(
                ApiResponse(code = 200, message = "Successful operation"),
                ApiResponse(code = 400, message = "Incorrect json"),
                ApiResponse(code = 401, message = "Incorrect userId"),
                ApiResponse(code = 404, message = "Habit or user not found"),
                ApiResponse(code = 500, message = "Server error")
        )
        fun update(@RequestParam id: Int, @RequestParam userId: Int, @RequestBody body: Habit): Habit =
                transaction {
                    if (body != null)

                        if (userId != null)
                            repository.update(userId, id, body)

                        else throw  ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Incorrect userID");

                        else throw  ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Incorrect json");

                }
    }
}


