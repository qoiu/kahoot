package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.model.Reply
import java.text.DateFormat
import java.util.Date

class DbAddMessage(private val db: DatabaseInterface.Executor) : DbExecutor.Base<Reply, Int> {

    override fun execute(objects: Reply): Int {
        val sql = "INSERT or IGNORE INTO Chat (user,time,message) VALUES (? , ? , ?)"
        val format = DateFormat.getDateTimeInstance().format(Date())
        return db.executeUpdate(sql, objects.userID,format,objects.text)
    }
}