package pl.alan.services.repository

import pl.alan.services.model.Habit


interface HabitDbRepository: CrudRepository<Habit, String>
