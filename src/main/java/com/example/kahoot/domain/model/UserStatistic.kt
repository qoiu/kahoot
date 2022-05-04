package com.example.kahoot.domain.model

interface UserStatistic {
    interface Add {
        fun addAnswer(isCorrect: Boolean, time: Long = 10L)
    }

    interface Read : Time {
        fun getStatistic(questionId: Int): Base.QuestionStatistic
    }

    interface Time {
        fun getTotalTime(): Double
        fun getTime(questionId: Int): Double
    }


    interface Full : Read, Add, Time, UserStatistic

    open class Base : Full {
        protected val list = mutableListOf<QuestionStatistic>()
        var score = 0

        override fun addAnswer(isCorrect: Boolean, time: Long) {
            list.add(QuestionStatistic.Base(isCorrect, time))
        }

        override fun getTotalTime(): Double {
            var result = 0.0
            list.forEachIndexed { i, _ ->
                result += getTime(i)
            }
            return result
        }

        override fun getTime(questionId: Int): Double {
            list[questionId].apply {
                return if (this is QuestionStatistic.Base) {
                    this.time / 10.0
                } else {
                    0.0
                }
            }
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

        sealed class QuestionStatistic {
            data class Base(
                val correct: Boolean,
                val time: Long
            ) : QuestionStatistic()

            object NoAnswer : QuestionStatistic()
        }
    }

    class Test : Base() {
        fun get() = list.toList()
    }

}