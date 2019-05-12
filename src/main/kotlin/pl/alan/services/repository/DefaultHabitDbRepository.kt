package pl.alan.services.repository

import com.mysql.jdbc.Connection
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import pl.alan.services.Habits
import pl.alan.services.model.Habit
import javax.xml.ws.Response

@Repository
class DefaultHabitDbRepository() : HabitDbRepository{


    override fun createTable() = SchemaUtils.create(Habits)

    override fun create(habit: Habit): Habit {
        Habits.insert(toRow(habit))
        return habit
    }

    fun update(userId: Int, id: Int, habit: Habit): Habit {
        Habits.update({((Habits.id eq id) and (Habits.userId eq userId))}){
            it[name] = habit.name
            it[habitType] = habit.habitType
            it[difficulty] = habit.difficulty
            it[score] = habit.score
            it[color] = habit.color
        }
        return habit
    }

    override fun findAll(userId: Int): List<Habit> =

            if (userId == 0)
                Habits.selectAll().map {
                fromRow(it)
            }else {
                Habits.select {(Habits.userId eq userId)}.map {
                    fromRow(it)
            }
    }



    override fun deleteAll() = Habits.deleteAll()

    private fun toRow(h: Habit): Habits.(UpdateBuilder<*>) -> Unit = {
        //it[id] = h.id
        it[userId] = h.userID
        it[name] = h.name
        it[habitType] = h.habitType
        it[difficulty] = h.difficulty
        it[score] = h.score
        it[color] = h.color


    }

    private fun fromRow(r: ResultRow) =
            Habit(r[Habits.id],
                    r[Habits.userId],
                    r[Habits.name],
                    r[Habits.habitType],
                    r[Habits.difficulty],
                    r[Habits.score],
                    r[Habits.color])


    private fun connectDatabase (){
        Database.connect("jdbc:mysql://127.0.0.1:3306/habits", driver = "com.mysql.jdbc.Driver")

    }

    fun findByUserId(userId: Int): List<Habit> =
                Habits.select {(Habits.userId eq userId)}.map {
                    fromRow(it)
                }

    fun findByUserId(userId: Int, id: Int): List<Habit> =
            Habits.select {((Habits.id eq id) and (Habits.userId eq userId))}.map {
                fromRow(it)
            }

    fun findUser(userId: Int): List <Habit> =
            Habits.select {(Habits.userId eq userId)}.map {
                fromRow(it)
            }


    fun deleteById(userId: Int ,id: Int) =
            Habits.deleteWhere {((Habits.id eq id) and  (Habits.userId eq userId))}

    fun getHabitColor(habit: Habit): Int {
        when {
            habit.score < 0 -> {
                habit.color = red;
                return habit.color
            }
            habit.score in 0..9 -> {
                habit.color = orange;
                return habit.color
            }
            habit.score in 10..39 -> {
                habit.color = yellow;
                return habit.color
            }
            habit.score in 40..49 -> {
                habit.color = green;
                return habit.color
            }
            50 <= habit.score -> {
                habit.color = blue;
                return habit.color
            }
            else -> return 0
        }
    }

    fun getScoreDelta (habit: Habit): Int{
        var scoreDelta : Int
        scoreDelta = habit.difficulty

        when (habit.difficulty) {
            easy -> scoreDelta = 2
            medium -> scoreDelta = 3
            hard -> scoreDelta = 5

        }
        return scoreDelta
    }

    fun updateHabitScore(userId: Int, id: Int, habit: Habit) : Habit {

        var scoreDelta = getScoreDelta(habit)
        var scoreCategory = getHabitColor(habit);
        var body = habit

        var defaultScoreAlgorithm = DefaultScoreAlgorithm()
        defaultScoreAlgorithm.updateScoreAlgorithm(scoreDelta, scoreCategory, body)

        transaction {
            body = update(habit.id!!, userId, habit)
        }
        return body
    }

    companion object {
        //Color
        const val red = 1
        const val orange = 2
        const val yellow = 3
        const val green = 4
        const val blue = 5

        //Type
        const val good = 1
        const val bad = 2
        const val both = 3

        //Difficulty
        const val easy = 1
        const val medium = 2
        const val hard = 3
    }
}



