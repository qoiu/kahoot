package com.example.kahoot.domain.model

import com.example.kahoot.domain.model.statistic.ChartGraphic

interface GameStatistic {
    fun userStatistic(user: User): UserStatistic.Add

    fun getRate(questionId: Int)

    fun allReady(questionId: Int): Boolean
    fun correctAnswers(user: User): Int
    fun updateQuestionCount(questionId: Int)

    fun forEachQuestion(action: (question: Int) -> Unit)
    fun userTime(questions: List<String>): ChartGraphic

    class Base(private val users: List<User>) : GameStatistic {
        private val statistic = HashMap<User, UserStatistic.Full>()
        private var currentQuestion = 0

        init {
            users.forEach { user ->
                statistic[user] = UserStatistic.Base()
            }
        }

        override fun userStatistic(user: User): UserStatistic.Add = getStatistic(user)

        private fun getStatistic(user: User): UserStatistic.Full {
            if (!statistic.containsKey(user) || statistic[user] == null) {
                statistic[user] = UserStatistic.Base()
            }
            return statistic[user]!!
        }

        override fun getRate(questionId: Int) {
            statistic.keys.forEach { user ->
//                getStatistic(user).
            }
        }

        override fun allReady(questionId: Int): Boolean {
            statistic.keys.forEach { user ->
                if (getStatistic(user).getStatistic(questionId) == UserStatistic.Base.QuestionStatistic.NoAnswer)
                    return false
            }
            return true
        }

        override fun correctAnswers(user: User): Int {
            var result = 0
            for (i in 0..currentQuestion) {
                val stat = getStatistic(user).getStatistic(i)
                if (stat is UserStatistic.Base.QuestionStatistic.Base)
                    result++
            }
            return result
        }

        override fun updateQuestionCount(questionId: Int) {
            currentQuestion = questionId
        }

        override fun forEachQuestion(action: (question: Int) -> Unit) {
            for (i in 0..currentQuestion) {
                action.invoke(i)
            }
        }

        override fun userTime(questions: List<String>): ChartGraphic {
            val chart = ChartGraphic.Base()
            users.forEach { user ->
                chart.addGroup(user.currentNick)
                for (i in 0..currentQuestion) {
                    chart.addObj(questions[i], getStatistic(user).getTime(i))
                }
                chart.addObj("Total", getStatistic(user).getTotalTime())
            }
            return chart
        }

        fun questionStat(question: KahootQuestion) {
            val chart = ChartGraphic.Base()
            question.answers.forEach { q ->
                users.forEach { user ->
                    getStatistic(user).getStatistic(currentQuestion)

                }
                chart.addGroup(q)
            }
        }
    }

//    class Test(
//        val users: List<User> = listOf(
//            User(0, "a", "a nick", "a tg", UserState.Null),
//            User(1, "b", "b nick", "b tg", UserState.Null),
//        )
//    ) : GameStatistic {
//
//        private val statistic = Base(users)
//
//        override fun userStatistic(user: User): UserStatistic.Add = statistic.userStatistic(user)
//
//
//        override fun getRate(questionId: Int) {
//            getRate(questionId)
//        }
//
//        override fun allReady(questionId: Int): Boolean = statistic.allReady(questionId)
//
//
//        override fun correctAnswers(user: User): Int = statistic.correctAnswers(user)
//
//        override fun updateQuestionCount(questionId: Int) = statistic.updateQuestionCount(questionId)
//        override fun userTime(user: User): UsersTimeChart = statistic.userTime(user)
//    }
}