package com.example.kahoot.domain.model

import com.example.kahoot.presentation.model.ChartGraphic

interface GameStatistic {

    fun userStatistic(user: User): UserStatistic.Add

    /**
     *show is all users answer question
     */
    fun allReady(questionId: Int): Boolean

    /**
     * update current question id
     */
    fun updateQuestionCount(questionId: Int)


    fun getCorrectAnswers(answer: Int): Int

    /**
     * map userTime to chart
     * should be separate mapper :(
     */
    fun userTimeChart(questions: List<String>): ChartGraphic<*, *>


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

        override fun userTimeChart(questions: List<String>): ChartGraphic<*, *> {
            val chart = ChartGraphic.Base<Double>()
            users.forEach { user ->
                chart.addGroup(user.currentNick)
                for (i in 0..currentQuestion) {
                    chart.addObj(questions[i], getStatistic(user).getTime(i) / 10.0)
                }
                chart.addObj("Total", getStatistic(user).getTotalTime() / 10.0)
            }
            return chart
        }
    }
}