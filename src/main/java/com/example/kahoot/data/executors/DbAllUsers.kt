package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.model.AdminDefaultState
import com.example.kahoot.domain.model.User
import com.example.kahoot.domain.model.UserState
import java.sql.SQLException
import java.util.*

class DbAllUsers(private val db: DatabaseInterface.Executor) : DbExecutor.All<Set<User>> {
    override fun execute(): Set<User> {
        val result = mutableSetOf<User>()
        val sql = "SELECT id, access, nick, nickTg, fullName FROM Users"
        val set = db.executeQuery(sql)
        try {
            while (set.next()) {
                val id = set.getLong("id")
                val status = set.getString("access")
                val nick1 = set.getString("nick")
                val nick2 = set.getString("nickTg")
                val name = set.getString("fullName")
                val queue = PriorityQueue<String>()
                queue.addAll(listOf(nick1, nick2))
                when (status) {
                    "STUDENT" -> result.add(User(id, nick1, nick2, name, UserState.Default(), status))
                    "ADMIN" -> result.add(User(id, nick1, nick2, name, AdminDefaultState(), status))
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return result
    }
}