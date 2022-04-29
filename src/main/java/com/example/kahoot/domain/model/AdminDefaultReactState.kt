package com.example.kahoot.domain.model

import com.example.kahoot.data.executors.DbAddKahoot
import com.example.kahoot.data.executors.DbAddQuestion
import com.example.kahoot.data.executors.DbAllKahoot

class AdminDefaultState : UserState() {
    override fun execute(message: String) {
        presenter.postMsg(
            Reply(
                user.id, "Hello ${user.currentNick}. As admin you can make this stuff", listOf(
                    Btn("Create Kahoot", "/addKahoot"),
                    Btn("Start Session", "/start")
                )
            )
        )
        user.currentState = AdminDefaultReactState()
    }
}

class AdminDefaultReactState : UserState() {
    override fun execute(message: String) {
        if (message == "/start") {
            DbAllKahoot(db).execute()
            presenter.postMsg(Reply(user.id, "Choose From kahoot", listOf(Btn("AddKahoot", "/addKahoot"))))
        }
        if (message == "/addKahoot") {
            presenter.postMsg(Reply(user.id, "Please enter kahoot name"))
            user.currentState = AdminAddNewKahoot()
        }
    }
}

class AdminAddNewKahoot : UserState() {
    override fun execute(message: String) {
        val kahoot = Kahoot(-1, message)
        presenter.postMsg(Reply(user.id, "Please enter question"))
        user.currentState = AdminAddNewKahootQuestion(kahoot)
    }
}


class AdminEditKahoot(private val kahoot: Kahoot) : UserState() {
    override fun execute(message: String) {
        presenter.postMsg(msg())
        user.currentState = AdminAddNewKahootQuestion(kahoot)
        when (message) {
            "/saveKahoot" -> {
                DbAddKahoot(db).execute(kahoot)
                presenter.postMsg(Reply(user.id, "Kahoot was added"))
                return
            }
        }
    }

    private fun msg() = Reply(user.id, "${kahoot.title}\nPlease enter new question", buildList {
        kahoot.questions.forEachIndexed { id, q ->
            this.add(Btn("Delete: (${kahoot.title}) ${q.question}", "/delQ_$id"))
        }
        this.add(Btn("Save kahoot", "/saveKahoot"))
        this.add(Btn("Delete kahoot", "/deleteKahoot"))
    })
}

class AdminAddNewKahootQuestion(private val kahoot: Kahoot) : UserState() {
    override fun execute(message: String) {
        kahoot.questions.forEachIndexed { id, q ->
            if (message == "/delQ_$id")
                kahoot.questions.remove(q)
        }
        when (message) {
            "/deleteKahoot" -> {
                // TODO: 05.04.2022
            }
            else -> {
                val question = KahootQuestion(message)
                presenter.postMsg(
                    Reply(
                        user.id, "${question.question}\nPlease enter correct answer", listOf(
                            Btn("Delete question", "/deleteQuestion"),
                            Btn("True/False: True", "/tftrue"),
                            Btn("True/False: false", "/tffalse")
                        )
                    )
                )
                user.currentState = AdminKahootAnswer(kahoot, question)
            }
        }
    }
}

class AdminKahootAnswer(private val kahoot: Kahoot, private val question: KahootQuestion) : UserState() {
    override fun execute(message: String) {
        if (message.startsWith("/")) {
            adminCmdReact(message)
        } else {
            question.answers.add(message)
            if (question.answers.isEmpty()) {
                presenter.postMsg(msgNoAnswers())
            } else {
                presenter.postMsg(msg())
            }
        }
    }

    private fun msg() = Reply(
        user.id, "$question\n\nPlease enter answer", buildList {
            if (question.correct.isNotEmpty())
                this.add(Btn("Save question", "/saveQuestion"))
            this.add(Btn("Pick right answer", "/pickAnswer"))
            this.add(Btn("Delete question", "/deleteQuestion"))
            this.add(Btn("Delete answer", "/delAnswer"))
//            question.answers.forEach { this.add(Btn("Delete: $it", "/del$it")) }
        }
    )

    private fun msgNoAnswers() = Reply(
        user.id, "${question.question}\nPlease enter correct answer", listOf(
            Btn("True/False: True", "/tftrue"),
            Btn("True/False: false", "/tffalse")
        )
    )

    private fun adminCmdReact(message: String) {
        question.answers.forEach {
            if (message == "/del$it") {
                question.answers.remove(it)
                presenter.postMsg(msg())
                return
            }
        }
        question.answers.forEach {
            if (message == "/pick$it") {
                question.correct = it
                presenter.postMsg(msg())
                return
            }
        }
        when (message) {
            "/pickAnswer" -> {
                presenter.postMsg(Reply(user.id, "Pick right answer", question.answers.map { Btn(it, "/pick$it") }))
            }
            "/delAnswer" -> {
                presenter.postMsg(Reply(user.id, "Delete answer", question.answers.map { Btn(it, "/del$it") }))
            }
            "/saveQuestion" -> {
                DbAddQuestion(db).execute(question)
                saveAndReturn()
            }
            "/deleteQuestion" -> {
                user.currentState = AdminEditKahoot(kahoot)
                user.currentState.execute("any")
            }
            "/tftrue" -> {
                question.answers.add("True")
                question.answers.add("False")
                DbAddQuestion(db).execute(question)
                saveAndReturn()

            }
            "/tffalse" -> {
                question.answers.add("False")
                question.answers.add("True")
                DbAddQuestion(db).execute(question)
                saveAndReturn()
            }
        }
    }

    private fun saveAndReturn() {
        kahoot.questions.add(question)
        user.currentState = AdminEditKahoot(kahoot)
        user.currentState.execute("any")
    }
}