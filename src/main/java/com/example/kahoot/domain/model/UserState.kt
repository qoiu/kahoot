package com.example.kahoot.domain.model

import com.example.kahoot.domain.clean.MainInteractor

sealed class UserState {
    protected lateinit var user: User
    fun initUser(user: User) {
        this.user = user
    }

    open fun execute(message: String) {
        println("${this.javaClass.simpleName}.execute($message) This fun is not override.")
    }

    open fun execute() {
        println("${this.javaClass.simpleName}.execute() This fun is not override.")
    }

    class Default : UserState() {
        override fun execute(message: String) {
            interactor.bot().postMsg(Reply(user.id, "Enter pin code", emptyList()))
        }
    }

    class Ready : UserState() {
        override fun execute(message: String) {
            user.currentNick = message
            interactor.repository().user().save(user)
            execute()
        }

        override fun execute() {
            interactor.bot().postMsg(
                Reply(
                    user.id,
                    "Now you ready to start\nYour name: <b>${user.currentNick}</b>\nIf you want, you may enter another name\n\nPlease wait for start...",
                    listOf(
                        Btn(user.nickTg, user.nickTg),
                        Btn(user.nameTg, user.nameTg)
                    )
                )
            )
        }
    }

    class QuestionResult(private val question: KahootQuestion) : UserState() {
        override fun execute() {
            val text = "${question.question}\nCorrect answer: ${question.question}!"
            interactor.bot().postMsg(Reply(user.id, text))
        }
    }

    class Answer(private val question: KahootQuestion, private val game: Game.Answer) : UserState() {
        override fun execute(message: String) {
            if (message.startsWith("/") && message[1].toString().toInt() in 0 until question.answers.size) {
                interactor.bot().postMsg(
                    Reply(
                        user.id,
                        "${question.question}\n${question.answers[message[1].toString().toInt()]}"
                    )
                )
                val answerId = message[1].toString().toInt()
                game.addAnswer(user, question.answers[answerId])
                user.currentState = NoReact
            }
        }

        override fun execute() {
            interactor.bot().postMsg(
                Reply(
                    user.id, question.question, listOf(
                        Btn(question.answers[0], "/0"),
                        Btn(question.answers[1], "/1"),
                        Btn(question.answers[2], "/2"),
                        Btn(question.answers[3], "/3")
                    )
                )
            )
        }
    }

    object Null : UserState() {
        override fun execute(message: String) {
            throw IllegalStateException("Null status call")
        }
    }

    object NoReact : UserState()

    companion object {
        @JvmStatic
        protected lateinit var interactor: MainInteractor
        fun init(interactor: MainInteractor) {
            this.interactor = interactor
        }
    }
}
