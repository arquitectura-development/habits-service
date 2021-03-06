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
class DefaultHabitDbRepository() : HabitDbRepository {


    override fun createTable() = SchemaUtils.create(Habits)

    override fun create(habit: Habit): Habit {
        Habits.insert(toRow(habit))
        return habit
    }

    override fun update(userId: Int, id: Int, habit: Habit): Habit {
        if (habit.difficulty == 0) {
            habit.difficulty = getDifficulty(userId, id)
        }
        if (habit.habitType == 0) {
            habit.habitType = getHabitType(userId, id)
        }
        if (habit.score == 0.0) {
            habit.score = getScore(userId, id)
        }
        habit.color = getColor(userId, id)

        Habits.update({ ((Habits.id eq id) and (Habits.userId eq userId)) }) {
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
                }
            else {
                Habits.select { (Habits.userId eq userId) }.map {
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


    override fun findByUserId(userId: Int): List<Habit> =
            Habits.select {(Habits.userId eq userId)}.map {
                fromRow(it)
            }

    override fun findByUserHabitId(userId: Int, id: Int): List<Habit> =
            Habits.select {((Habits.id eq id) and (Habits.userId eq userId))}.map {
                fromRow(it)
            }

    override fun deleteById(userId: Int ,id: Int) =
            Habits.deleteWhere {((Habits.id eq id) and  (Habits.userId eq userId))}

    override fun getHabitColor(habit: Habit): Int {
        when {
            habit.score < 0 -> {
                habit.color = RED;
                return habit.color
            }
            habit.score in 0..9 -> {
                habit.color = ORANGE;
                return habit.color
            }
            habit.score in 10..39 -> {
                habit.color = YELLOW;
                return habit.color
            }
            habit.score in 40..49 -> {
                habit.color = GREEN;
                return habit.color
            }
            50 <= habit.score -> {
                habit.color = BLUE;
                return habit.color
            }
            else -> return 0
        }
    }

    override fun getScoreDelta (habit: Habit): Int{
        var scoreDelta : Int
        scoreDelta = habit.difficulty

        when (habit.difficulty) {
            EASY -> scoreDelta = 2
            MEDIUM -> scoreDelta = 3
            HARD -> scoreDelta = 5

        }
        return scoreDelta
    }

    override fun updateHabitScore(userId: Int, id: Int, positive : Boolean, habit: Habit) : Habit {
        habit.score = getScore(userId, id)
        habit.difficulty = getDifficulty(userId, id)
        habit.habitType = getHabitType(userId, id)
        habit.color = getColor(userId, id)
        var scoreDelta = getScoreDelta(habit)
        var scoreCategory = getHabitColor(habit);
        var body = habit

        var defaultScoreAlgorithm = DefaultScoreAlgorithm()
        defaultScoreAlgorithm.updateScoreAlgorithm(habit.score, scoreDelta, scoreCategory, positive, body)

        Habits.update({((Habits.id eq id) and (Habits.userId eq userId))}){
            it[score] = habit.score
        }
        return body
    }

    override fun getScore(userId: Int, id: Int): Double {

        var list = Habits.select {((Habits.id eq id) and (Habits.userId eq userId))}.map {
            fromRow(it)
        }
        return list[0].score

    }

    override fun getDifficulty(userId: Int, id: Int): Int {

        var list = Habits.select {((Habits.id eq id) and (Habits.userId eq userId))}.map {
            fromRow(it)
        }
        return list[0].difficulty

    }

    override fun getHabitType(userId: Int, id: Int): Int {

        var list = Habits.select {((Habits.id eq id) and (Habits.userId eq userId))}.map {
            fromRow(it)
        }
        return list[0].habitType

    }

    override fun getColor (userId: Int, id: Int): Int {

        var list = Habits.select {((Habits.id eq id) and (Habits.userId eq userId))}.map {
            fromRow(it)
        }
        return list[0].color

    }

    companion object {
        //Color
        const val RED = 1
        const val ORANGE = 2
        const val YELLOW = 3
        const val GREEN = 4
        const val BLUE = 5

        //Type
        const val GOOD = 1
        const val BAD = 2
        const val BOTH = 3

        //Difficulty
        const val EASY = 1
        const val MEDIUM = 2
        const val HARD = 3
    }
}



