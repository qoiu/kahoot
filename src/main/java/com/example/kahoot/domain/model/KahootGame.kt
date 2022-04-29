package com.example.kahoot.domain.model

class KahootGame(private val kahoot: Kahoot, private val users: List<User>) {
    private var question: Int = 0


    fun getQuestion() = kahoot.questions[question]

    fun nextQuestion() {
        if (question < kahoot.questions.size)
            question++
    }

    fun users(action: (user: User) -> Unit) {
        users.forEach { action.invoke(it) }
    }

    fun isLastQuestion() = question < kahoot.questions.size
}