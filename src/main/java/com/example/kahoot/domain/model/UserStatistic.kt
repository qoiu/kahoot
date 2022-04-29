package com.example.kahoot.domain.model

interface UserStatistic {
    interface Add {
        fun addAnswer(isCorrect: Boolean, time: Long = 10L)
    }

    interface Read {
        fun getStatistic(questionId: Int): Base.QuestionStatistic
    }


    interface Full : Read, Add, UserStatistic

    open class Base : Full {
        protected val list = mutableListOf<QuestionStatistic>()

        override fun addAnswer(isCorrect: Boolean, time: Long) {
            list.add(QuestionStatistic.Base(isCorrect, time))
        }

        override fun getStatistic(questionId: Int): QuestionStatistic =
            if (questionId in 0 until list.size) {
                list[questionId]
            } else {
                QuestionStatistic.NoAnswer
            }

        override fun toString(): String {
            return list.toString()
        }

        sealed class QuestionStatistic() {
            data class Base(
                private val correct: Boolean,
                private val time: Long
            ) : QuestionStatistic()

            object NoAnswer : QuestionStatistic()
        }
    }

    class Test(): Base() {
        fun get() = list.toList()
    }

}