package com.example.kahoot.domain.model

interface GameStatistic {
    fun saveAnswer(user: User, id: Int, time: Long)
}