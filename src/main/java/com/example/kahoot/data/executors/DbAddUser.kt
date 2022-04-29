package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.model.User

class DbAddUser(private val db: DatabaseInterface.Executor) : DbExecutor.Base<User, Int> {
    override fun execute(objects: User): Int {
        val sqlDelete = "DELETE FROM Users Where id = ${objects.id}"
        db.execute(sqlDelete)
        val sql = "INSERT or IGNORE INTO Users (id,nick,nickTg,fullName) VALUES (? , ? , ? , ? )"
        return db.executeUpdate(sql, objects.id, objects.currentNick, objects.nickTg, objects.nameTg)
    }
}