package com.example.kahoot.domain.model

interface UserStatistic {
    interface Add {
        fun addAnswer(isCorrect: Boolean, answerId: Int, time: Long = 10L)
        fun noAnswer()
    }

    interface Read : Time {
        fun getStatistic(questionId: Int): Base.QuestionStatistic
        fun score(): Int
    }

    interface Time {
        fun getTotalTime(): Double
        fun getTime(questionId: Int): Double
    }


    interface Full : Read, Add, Time, UserStatistic


    open class Base(private val roundTime: Int) : Full {
        protected val list = mutableListOf<QuestionStatistic>()
        private var score = 0
        private var strike = 0

        override fun addAnswer(isCorrect: Boolean, answerId: Int, time: Long) {
            updateStrike(isCorrect)
            val additionalScore = if (isCorrect)
                ((roundTime * 100 - time) * (1 + (0.01 * strike))).toInt()
            else
                0
            score += additionalScore
            list.add(QuestionStatistic.Base(isCorrect, answerId, time, additionalScore))
        }

        override fun noAnswer() {
            list.add(QuestionStatistic.NoAnswer)
            updateStrike(false)
        }

        override fun score(): Int = score / 10

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
                val answerId: Int,
                val time: Long,
                val score: Int = 0
            ) : QuestionStatistic()

            object NoAnswer : QuestionStatistic()
        }

        private fun updateStrike(isCorrect: Boolean) {
            if (!isCorrect) {
                strike = 0
            } else {
                if (list.size > 0 && list[list.size - 1] is QuestionStatistic.Base)
                    if ((list[list.size - 1] as QuestionStatistic.Base).correct)
                        strike++
            }
        }
    }

    class Test : Base(15) {
        fun get() = list.toList()
    }

}