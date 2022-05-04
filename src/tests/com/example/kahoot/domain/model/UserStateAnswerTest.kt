package com.example.kahoot.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserStateAnswerTest : BaseTest() {

    private val question = KahootQuestion("Question", listOf("1", "2", "3", "4").toMutableList())
    private val user = User(0, "a", "a", "s", UserState.Null)
    private val game = TestGame()
    private val state = UserState.Answer(question, game)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        user.currentState = state
    }

    @Test
    fun execute() {
        state.execute("")
        assertEquals(0, game.answerAmount)
        assertEquals(state, user.currentState)
        assertEquals("", presenter.actualMsg)
        state.execute("TorsosDSP")
        assertEquals(0, game.answerAmount)
        assertEquals(state, user.currentState)
        state.execute("/5")
        assertEquals(0, game.answerAmount)
        assertEquals(state, user.currentState)
        state.execute("/4")
        assertEquals(0, game.answerAmount)
        assertEquals(state, user.currentState)
        state.execute("/3")
        assertEquals(1, game.answerAmount)
        assertEquals(UserState.NoReact, user.currentState)
        assertEquals("Question\n4", presenter.actualReply.text)
        assertEquals(UserState.NoReact.javaClass, user.currentState.javaClass)
    }

    @Test
    fun answerTwo() {
        state.execute("/1")
        assertEquals(1, game.answerAmount)
        assertNotEquals(user.currentState, state)
        assertEquals(presenter.actualReply.text, "Question\n2")
        assertEquals(user.currentState.javaClass, UserState.NoReact.javaClass)
    }

    class TestGame : Game.Answer {
        var answerAmount: Int = 0
        var lastAnswer = ""
        override fun addAnswer(user: User, answer: String) {
            answerAmount++
            lastAnswer = answer
        }
    }
}