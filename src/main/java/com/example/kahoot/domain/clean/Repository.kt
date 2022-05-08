package com.example.kahoot.domain.clean

interface Repository<T> {
    fun save(data: T): Long
    fun read(conditions: Conditions = Conditions.Empty): List<T>
}