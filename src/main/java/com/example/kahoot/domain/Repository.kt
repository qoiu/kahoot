package com.example.kahoot.domain

interface Repository<T> {
    fun save(data: T)
    fun read(): List<T>
}