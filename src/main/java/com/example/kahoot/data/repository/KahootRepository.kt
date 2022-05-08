package com.example.kahoot.data.repository

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.data.executors.DbAddKahoot
import com.example.kahoot.data.executors.DbAllQuestions
import com.example.kahoot.domain.clean.Conditions
import com.example.kahoot.domain.clean.Repository
import com.example.kahoot.domain.model.Kahoot
import java.sql.SQLException

class KahootRepository(private val db: DatabaseInterface.Executor) : Repository<Kahoot> {
    override fun save(data: Kahoot): Long = DbAddKahoot(db).execute(data)

    override fun read(conditions: Conditions): List<Kahoot> {
        val result = mutableListOf<Kahoot>()
        val sql = "SELECT id, title FROM Kahoot ${conditions.toSql()}"
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