package com.example.kahoot.domain.model.statistic

import com.example.kahoot.domain.model.User

data class UsersTimeChart(
    val user: List<User>,
    val qAmount: Int,
    val list: MutableList<Series> = mutableListOf()
) {

    fun totalTime(): List<Row> {
        val mutableList = mutableListOf<Row>()
        user.forEach {
            mutableList.add(totalTime(it))
        }
        return mutableList.toList()
    }

    fun totalTime(user: User): Row {
        var result = 0L
        list.forEach { series ->
            series.list.forEach { row ->
                if (row.user == user)
                    result += row.value
            }
        }
        return Row(user, result)
    }

    data class Series(
        val qId: Int,
        val list: MutableList<Row> = mutableListOf()
    )

    data class Row(
        val user: User,
        val value: Long
    )
}