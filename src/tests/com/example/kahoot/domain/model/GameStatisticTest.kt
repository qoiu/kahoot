package com.example.kahoot.domain.model

import org.junit.jupiter.api.Test

internal class GameStatisticTest : BaseTest() {

    @Test
    fun usersAddAnswer() {
//        val statistic = GameStatistic.Test()
//        statistic.users
    }

    @Test
    fun allReady() {
//        val statistic = GameStatistic.Test()
//        assertEquals(false,statistic.allReady(0))
//        assertEquals(false,statistic.allReady(1))
//        statistic.userStatistic(statistic.users[0]).addAnswer(true)
//        assertEquals(false,statistic.allReady(0))
//        assertEquals(false,statistic.allReady(1))
//        statistic.userStatistic(statistic.users[1]).addAnswer(true)
//        assertEquals(true,statistic.allReady(0))
//        assertEquals(false,statistic.allReady(1))
    }

    class TestGame : Game.Answer {
        override fun addAnswer(user: User, answer: String) {

        }
    }
}