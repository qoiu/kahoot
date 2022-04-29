package com.example.kahoot.domain.model

import com.example.kahoot.data.DatabaseInterface.Executor
import com.example.kahoot.data.executors.DbAddUser
import com.example.kahoot.domain.presesnter.MainPresenter

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
            presenter.postMsg(Reply(user.id, "Enter pin code", emptyList()))
        }
    }

    class Ready : UserState() {
        override fun execute(message: String) {
            user.currentNick = message
            DbAddUser(db).execute(user)
            execute()
        }

        override fun execute() {
            presenter.postMsg(
                Reply(
                    user.id,
                    "Now you ready to start\nYour name: **${user.currentNick}**\nIf you want, you may enter another name\n\nPlease wait for start...",
                    listOf(
                        Btn(user.currentNick, user.currentNick),
                        Btn(user.nickTg, user.nickTg),
                        Btn(user.nameTg, user.nameTg)
                    )
                )
            )
        }
    }

    class Answer(private val question: KahootQuestion) : UserState() {
        override fun execute(message: String) {
            if (message.startsWith("/")) {
                presenter.postMsg(
                    Reply(
                        user.id,
                        "${question.question} \n ${question.answers[message[1].toString().toInt()]}"
                    )
                )
            }
        }

        override fun execute() {
            presenter.postMsg(
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

    companion object {
        @JvmStatic
        protected lateinit var db: Executor

        @JvmStatic
        protected lateinit var presenter: MainPresenter
        fun init(db: Executor, presenter: MainPresenter) {
            Companion.db = db
            Companion.presenter = presenter
        }
    }
}
