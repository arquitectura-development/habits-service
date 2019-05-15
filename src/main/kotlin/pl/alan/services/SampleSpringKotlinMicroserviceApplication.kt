package pl.alan.services


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class SampleSpringKotlinMicroserviceApplication

object Habits : Table() {
    val id = integer("ID").primaryKey().autoIncrement()
    val userId = integer("userId").nullable()
    val name = varchar("name", length = 256)
    val habitType = integer("habitType")
    val difficulty = integer("difficulty")
    val score = double("score")
    val color = integer("color")
}


fun main(args: Array<String>) {
    Database.connect("jdbc:mysql://sql3.freemysqlhosting.net/sql3291433", driver = "com.mysql.jdbc.Driver", user = "sql3291433", password = "p9j5YyqdNy")



    runApplication<SampleSpringKotlinMicroserviceApplication>(*args)

}