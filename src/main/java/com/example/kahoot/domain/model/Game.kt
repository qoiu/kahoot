package com.example.kahoot.domain.model

interface Game {
    fun getRoundTime(): Int
    fun getQuestion(): KahootQuestion
    fun getQuestions(): List<String>
    fun nextQuestion()
    fun startQuestion(stoppedAction: () -> Unit)
    fun endQuestion()
    fun isLastQuestion(): Boolean
    fun forEachUser(action: (user: User) -> Unit)
    fun forEachAnsweredQuestion(action: (id: Int, question: KahootQuestion) -> Unit)

    interface Answer {
        fun addAnswer(user: User, answer: String)
    }

    interface Statistic {
        fun statistic(): GameStatistic
        fun statistic(user: User): UserStatistic.Read
    }

    interface Full : Game, Statistic, Answer


    object Test {
        private val users = listOf(
            User(0, "a", "a", "s", UserState.Null),
            User(0, "a", "a", "s", UserState.Null)
        )

        fun getGame(statistic: GameStatistic = GameStatistic.Base(users, 15)): KahootGame {
            return KahootGame(
                Kahoot(
                    0, "kahoot", mutableListOf(
                        KahootQuestion("q1", mutableListOf("1", "2", "3", "4"), "1"),
                        KahootQuestion("q2", mutableListOf("1", "2", "3", "4"), "2")
                    )
                ),
                users,
                statistic
            )
        }
    }
}