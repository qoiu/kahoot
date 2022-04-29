package com.example.kahoot.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserStateAnswerTest : BaseTest() {

    private val question = KahootQuestion("Question", listOf("1", "2", "3", "4").toMutableList())
    private val user = User(0, "a", "a", "s", UserState.Null)
    private val statistic = UserStatistic.Test()
    private val state = UserState.Answer(question, statistic)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        user.currentState = state
    }

    @Test
    fun execute() {
        state.execute("")
        assertEquals(statistic.get().size, 0)
        assertEquals(state, user.currentState)
        assertEquals(presenter.actualMsg, "")
        state.execute("TorsosDSP")
        assertEquals(statistic.get().size, 0)
        assertEquals(state, user.currentState)
        state.execute("/5")
        assertEquals(statistic.get().size, 0)
        assertEquals(state, user.currentState)
        state.execute("/4")
        assertEquals(statistic.get().size, 0)
        assertEquals(state, user.currentState)
        state.execute("/3")
        assertEquals(statistic.get().size, 1)
        assertNotEquals(user.currentState, state)
        assertEquals(presenter.actualReply.text, "Question\n4")
        assertEquals(user.currentState.javaClass, UserState.NoReact.javaClass)
    }

    @Test
    fun answerTwo() {
        state.execute("/1")
        assertEquals(statistic.get().size, 1)
        assertNotEquals(user.currentState, state)
        assertEquals(presenter.actualReply.text, "Question\n2")
        assertEquals(user.currentState.javaClass, UserState.NoReact.javaClass)
    }
}