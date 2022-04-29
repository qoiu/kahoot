package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import java.sql.SQLException

class DbAllAnswers(private val db: DatabaseInterface.Executor) : DbExecutor.Get<List<String>> {
    override fun execute(id: Long): List<String> {
        val result = mutableListOf<String>()
        val sql = "SELECT text FROM Answer WHERE qid=$id"
        val set = db.executeQuery(sql)
        try {
            while (set.next()) {
                result.add(set.getString("text"))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return result
    }
}