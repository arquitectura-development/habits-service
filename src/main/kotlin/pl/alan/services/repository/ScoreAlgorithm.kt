package pl.alan.services.repository

import pl.alan.services.model.Habit

interface ScoreAlgorithm {
    fun updateScoreAlgorithm(score: Double, scoreDelta: Int, scoreCategory: Int, body: Habit) : Double
}