package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.model.Kahoot
import java.sql.SQLException

class DbAllKahoot(private val db: DatabaseInterface.Executor) : DbExecutor.All<List<Kahoot>> {
    override fun execute(): List<Kahoot> {
        val result = mutableListOf<Kahoot>()
        val sql = "SELECT id, title FROM Kahoot"
        val set = db.executeQuery(sql)
        try {
            while (set.next()) {
                val id = set.getLong("id")
                val title = set.getString("title")
                val questions = DbAllQuestions(db).execute(id)
                result.add(Kahoot(id, title, questions.toMutableList()))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return result
    }
}