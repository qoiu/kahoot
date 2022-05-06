package com.example.kahoot.domain.model

import kotlin.random.Random

interface GameStatistic {

    fun editUserStatistic(user: User): UserStatistic.Add
    fun readUserStatistic(user: User): UserStatistic.Read

    /**
     *show is all users answer question
     */
    fun allReady(questionId: Int): Boolean

    /**
     * update current question id
     */
    fun updateQuestionCount(questionId: Int)


    fun getCorrectAnswers(answer: Int): Int

    fun sortedScoreboardUser(): List<User>

    open class Base(private val users: List<User>, private val roundTime: Int) : GameStatistic {
        private val statistic = HashMap<User, UserStatistic.Full>()
        private var currentQuestion = 0

        init {
            users.forEach { user ->
                statistic[user] = UserStatistic.Base(roundTime)
            }
        }

        override fun editUserStatistic(user: User): UserStatistic.Add = getStatistic(user)
        override fun readUserStatistic(user: User): UserStatistic.Read = getStatistic(user)

        private fun getStatistic(user: User): UserStatistic.Full {
            if (!statistic.containsKey(user) || statistic[user] == null) {
                statistic[user] = UserStatistic.Base(roundTime)
            }
            return statistic[user]!!
        }

        override fun allReady(questionId: Int): Boolean {
            statistic.keys.forEach { user ->
                if (getStatistic(user).getStatistic(questionId) == UserStatistic.Base.QuestionStatistic.NoAnswer)
                    return false
            }
            return true
        }

        override fun updateQuestionCount(questionId: Int) {
            currentQuestion = questionId
        }

        override fun getCorrectAnswers(answer: Int): Int {
            var result = 0
            users.forEach { user ->
                val stat = getStatistic(user).getStatistic(currentQuestion)
                if (stat is UserStatistic.Base.QuestionStatistic.Base && stat.answerId == answer)
                    result++
            }
            return result
        }

        override fun sortedScoreboardUser(): List<User> = statistic.keys.sortedBy { getStatistic(it).score() }
    }

    class Test : Base(users, 10) {
        init {
            users.forEach {
                editUserStatistic(it).addAnswer(true, 1, Random.nextLong(200))
            }
        }
    }

    private companion object {
        val users = listOf(
            User(1, "Vasya"),
            User(2, "Petya"),
            User(3, "Kolya"),
            User(4, "Vanka"),
        )
    }
}