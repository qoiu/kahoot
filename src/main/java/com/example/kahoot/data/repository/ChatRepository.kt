package com.example.kahoot.data.repository

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.clean.Conditions
import com.example.kahoot.domain.clean.Repository
import com.example.kahoot.domain.model.Reply
import java.sql.SQLException
import java.text.DateFormat
import java.util.*

class ChatRepository(private val db: DatabaseInterface.Executor) : Repository<Reply> {
    override fun save(data: Reply): Long {
        val sql = "INSERT or IGNORE INTO Chat (user,time,message) VALUES (? , ? , ?)"
        val format = DateFormat.getDateTimeInstance().format(Date())
        return db.executeUpdate(sql, data.userID, format, data.text)
    }

    override fun read(conditions: Conditions): List<Reply> {
        val sql = "SELECT user, time, message FROM Chat ${conditions.toSql()}"
        val set = db.executeQuery(sql)
        val result = mutableListOf<Reply>()
        try {
            while (set.next()) {
                val id = set.getLong("id")
                val message = set.getString("message")
                result.add(Reply(id, message))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return result
    }
}