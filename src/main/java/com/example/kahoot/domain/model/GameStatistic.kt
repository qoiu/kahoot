package com.example.kahoot.domain.model

interface GameStatistic {
    fun userStatistic(user: User): UserStatistic.Add

    fun getRate(questionId: Int)

    open class Base(users: List<User>): GameStatistic{
        private val statistic = HashMap<User, UserStatistic.Full>()
        init {
            users.forEach{user->
                statistic[user] = UserStatistic.Base()
            }
        }
        override fun userStatistic(user: User): UserStatistic.Add {
            if (!statistic.containsKey(user) || statistic[user] == null) {
                statistic[user] = UserStatistic.Base()
            }
            return statistic[user]!!
        }

        override fun getRate(questionId: Int) {
            statistic.keys.forEach { user->
//                getStatistic(user).
            }
        }
    }

    class Test(
        users: List<User> = listOf(
        User(0, "a", "a nick", "a tg", UserState.Null),
        User(1, "b", "b nick", "b tg", UserState.Null),
    )): Base(users){

    }
}