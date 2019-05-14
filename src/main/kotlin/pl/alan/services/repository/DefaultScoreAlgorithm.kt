package pl.alan.services.repository

import pl.alan.services.model.Habit
import pl.alan.services.Habits
import pl.alan.services.Habits.habitType
import pl.alan.services.Habits.score


open class DefaultScoreAlgorithm() : ScoreAlgorithm {
    override fun updateScoreAlgorithm(score: Double, scoreDelta: Int, scoreCategory: Int, positive: Boolean, habit: Habit): Double {
        habit.score = score
        if (habit.habitType == DefaultHabitDbRepository.both )
            if (positive == true){
                when (scoreCategory) {
                    4 -> habit.score += scoreDelta / 2
                    5 -> habit.score += 1
                    else -> habit.score += scoreDelta
                }
            }
            else  {
                when (scoreCategory) {
                    2 -> habit.score -= scoreDelta * 1.5
                    1 -> habit.score -= scoreDelta * 2
                    else -> habit.score -= scoreDelta
                }
            }
        else if (habit.habitType == DefaultHabitDbRepository.good) {
            when (scoreCategory) {
                4 -> habit.score += scoreDelta / 2
                5 -> habit.score += 1
                else -> habit.score += scoreDelta
            }
        } else if (habit.habitType == DefaultHabitDbRepository.bad) {
            when (scoreCategory) {
                2 -> habit.score -= scoreDelta * 1.5
                1 -> habit.score -= scoreDelta * 2
                else -> habit.score -= scoreDelta
            }
        }
        return habit.score
    }
}