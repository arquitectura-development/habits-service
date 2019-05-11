package pl.alan.services


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class SampleSpringKotlinMicroserviceApplication

object Habits : Table() {
    val id = integer("ID").primaryKey().autoIncrement() // Column<Int>
    val userId = integer("userId").nullable() // Column<Int?>
    val name = varchar("name", length = 256) // Column<Char>
    val habitType = integer("habitType") // Column<Int>
    val difficulty = integer("difficulty") // Column<Int>
    val score = double("score") // Column <Double>
    val color = integer("color") // Column<Int>
}

object Cities : Table() {
    val id = integer("id").autoIncrement().primaryKey() // Column<Int>
    val name = varchar("name", 50) // Column<String>
}

fun main(args: Array<String>) {
    Database.connect("jdbc:mysql://127.0.0.1:3306/habits", driver = "com.mysql.jdbc.Driver")

    runApplication<SampleSpringKotlinMicroserviceApplication>(*args)


/*
    transaction {
        SchemaUtils.create(Cities)
        Cities.insert {
            it[name] = "Prague"
        }

        Habit.insert {
        it[userId] = 123245
        it[name] = "Hacer codigo"
        it[habitType] = 2
        it[difficulty] = 1
        it[score] = 3.0
        it[color] = 2
        }
;
        Habit.selectAll().forEach {
            println("${it[Habit.name]}")
        }


    }

 */
}