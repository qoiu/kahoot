package com.example.kahoot.domain.model

import com.example.kahoot.domain.model.UserState.Answer
import java.util.*

class KahootGame(
    private val kahoot: Kahoot,
    private val users: List<User>,
    private val statistic: GameStatistic = GameStatistic.Base(users)
) : Game.Full {

    constructor(kahoot: Kahoot, users: List<User>) : this(kahoot, users, GameStatistic.Base(users))

    private var question: Int = 0
    private var timer: Long = 0
    private var stoppedAction: () -> Unit = {}

    override fun getQuestion() = kahoot.questions[question]
    override fun getQuestions(): List<String> = kahoot.questions.map { it.question }


    override fun nextQuestion() {
        if (question < kahoot.questions.size)
            question++
        statistic.updateQuestionCount(question)
    }

    override fun addAnswer(user: User, answer: String) {
        var answerId: Int = -1
        getQuestion().answers.forEachIndexed { index, s ->
            if (answer == s)
                answerId = index
        }
        statistic.userStatistic(user).addAnswer(
            getQuestion().correct == answer,
            answerId,
            (Date().time - timer) / 10
        )
        println("answer: $answer, time: ${(Date().time - timer) / 1000}")
        if (statistic.allReady(question))
            stoppedAction.invoke()

    }

    override fun startQuestion(stoppedAction: () -> Unit) {
        this.stoppedAction = stoppedAction
        users.forEach { user: User ->
            user.currentState = Answer(getQuestion(), this)
            user.execute()
        }
        timer = Date().time
    }

    fun forEachUser(action: (user: User) -> Unit) {
        users.forEach { action.invoke(it) }
    }

    override fun isLastQuestion() = question < kahoot.questions.size
    override fun statistic(): GameStatistic = statistic
}