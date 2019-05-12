package pl.alan.services.repository

import pl.alan.services.model.Habit

interface ScoreAlgorithm {
    fun updateScoreAlgorithm(scoreDelta: Int, scoreCategory: Int, body: Habit) : Habit
}