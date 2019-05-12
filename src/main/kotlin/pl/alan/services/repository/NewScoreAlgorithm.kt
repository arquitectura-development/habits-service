package pl.alan.services.repository

import pl.alan.services.model.Habit

class NewScoreAlgorithm() : ScoreAlgorithm {
    override fun updateScoreAlgorithm(scoreDelta: Int, scoreCategory: Int, habit: Habit): Habit {
        if (habit.habitType == DefaultHabitDbRepository.good) {
            when (scoreCategory) {
                4 -> habit.score += scoreDelta / 1
                5 -> habit.score += 5
                else -> habit.score += scoreDelta
            }
        } else if (habit.habitType == DefaultHabitDbRepository.bad) {
            when (scoreCategory) {
                2 -> habit.score -= scoreDelta * 2
                1 -> habit.score -= scoreDelta * 5
                else -> habit.score -= scoreDelta
            }
        }
        return habit
    }
}