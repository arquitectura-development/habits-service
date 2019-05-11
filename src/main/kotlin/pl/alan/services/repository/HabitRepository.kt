package pl.alan.services.repository

import org.springframework.stereotype.Repository
import pl.alan.services.model.Habit

@Repository
class HabitRepository {
    val habits = mutableListOf<Habit>()

    fun findById(id: Int): Habit? {
        return habits.singleOrNull { it.id == id }
    }

    fun findAll(): List<Habit> {
        return habits
    }

    fun save(habit: Habit): Habit {
        habit.id = (habits.maxBy { it.id!! }?.id ?: 0) + 1
        habits.add(habit)
        return habit
    }

    fun update(habit: Habit): Habit {
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index >= 0) {
            habits[index] = habit
        }
        return habit
    }

    fun removeById(id: Int): Boolean {
        return habits.removeIf { it.id == id }
    }

    fun getHabitColor(habit: Habit): Int {
        when {
            habit.score < 0 -> {
                habit.color = 1;
                return habit.color
            }
            habit.score in 0..9 -> {
                habit.color = 2;
                return habit.color
            }
            habit.score in 10..39 -> {
                habit.color = 3;
                return habit.color
            }
            habit.score in 40..49 -> {
                habit.color = 4;
                return habit.color
            }
            50 <= habit.score -> {
                habit.color = 5;
                return habit.color
            }
            else -> return 0
        }
    }

    fun updateHabitScore(habit: Habit) {
        var scoreDelta = habit.score
        var scoreCategory = getHabitColor(habit);
        if (habit.habitType == 1) {
            when (scoreCategory) {
                4 -> habit.score += scoreDelta / 2
                5 -> habit.score += 1
                else -> habit.score += scoreDelta
            }
        } else {
            when (scoreCategory) {
                2 -> habit.score -= scoreDelta * 1.5
                1 -> habit.score -= scoreDelta * 2
                else -> habit.score -= scoreDelta
            }
        }
    }
}
