package pl.alan.services


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class SampleSpringKotlinMicroserviceApplication

object Habits : Table() {
    val id = integer("ID").primaryKey().autoIncrement()  // Column<Int>
    val userId = integer("userId").nullable()            // Column<Int?>
    val name = varchar("name", length = 256)             // Column<Char>
    val habitType = integer("habitType")                 // Column<Int>
    val difficulty = integer("difficulty")               // Column<Int>
    val score = double("score")                          // Column <Double>
    val color = integer("color")                         // Column<Int>
}


fun main(args: Array<String>) {
    //Database.connect("jdbc:mysql://127.0.0.1:3306/habits", driver = "com.mysql.jdbc.Driver")
    Database.connect("jdbc:mysql://sql3.freemysqlhosting.net/sql3291433", driver = "com.mysql.jdbc.Driver",
    user = "sql3291433", password = "p9j5YyqdNy")



    runApplication<SampleSpringKotlinMicroserviceApplication>(*args)

}