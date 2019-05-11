package pl.alan.services.repository

import java.awt.Point

interface CrudRepository<T, K> {
    fun createTable()
    fun create(m: T): T
    fun findAll(): Iterable<T>
    fun deleteAll(): Int
}

