package pl.alan.services.repository

import pl.alan.services.model.Habit


interface HabitDbRepository: CrudRepository<Habit, String>{
    fun update(userId: Int, id: Int, habit: Habit): Habit
    fun getColor (userId: Int, id: Int): Int
    fun getHabitType (userId: Int, id: Int): Int
    fun getDifficulty (userId: Int, id: Int): Int
    fun getScore (userId: Int, id: Int): Double
    fun updateHabitScore(userId: Int, id: Int, positive : Boolean, habit: Habit) : Habit
    fun getScoreDelta (habit: Habit): Int
    fun getHabitColor(habit: Habit): Int
    fun findByUserId(userId: Int): List<Habit>
    fun findByUserHabitId(userId: Int, id: Int): List<Habit>
    fun deleteById(userId: Int ,id: Int): Int
}
