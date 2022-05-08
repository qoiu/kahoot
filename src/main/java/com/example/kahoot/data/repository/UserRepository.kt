package com.example.kahoot.data.repository

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.clean.Conditions
import com.example.kahoot.domain.clean.Repository
import com.example.kahoot.domain.model.User
import com.example.kahoot.domain.model.UserState
import java.sql.SQLException
import java.util.*

class UserRepository(private val db: DatabaseInterface.Executor) : Repository<User> {

    private val users = mutableListOf<User>()
    override fun save(data: User): Long {
        users.add(data)
        val sqlDelete = "DELETE FROM Users WHERE id = ${data.id}"
        db.execute(sqlDelete)
        val sql = "INSERT or IGNORE INTO Users (id,nick,nickTg,fullName) VALUES (? , ? , ? , ? )"
        return db.executeUpdate(sql, data.id, data.currentNick, data.nickTg, data.nameTg)
    }

    override fun read(conditions: Conditions): List<User> {
        if (users.isEmpty()) users.addAll(getAllUsers(conditions))
        if (conditions is Conditions.Base) {
            val result = conditions.get("id")?.let { value ->
                users.find { it.id == value }
            }
            return listOfNotNull(result)
        }
        return users
    }

    private fun getAllUsers(conditions: Conditions): MutableList<User> {
        val sql = "SELECT id, nick, nickTg, fullName FROM Users ${conditions.toSql()}"
        val set = db.executeQuery(sql)
        val result = mutableListOf<User>()
        try {
            while (set.next()) {
                val id = set.getLong("id")
                val nick1 = set.getString("nick")
                val nick2 = set.getString("nickTg")
                val name = set.getString("fullName")
                val queue = PriorityQueue<String>()
                queue.addAll(listOf(nick1, nick2))
                val state = UserState.Default()
                result.add(User(id, nick1, nick2, name, state))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return result
    }
}