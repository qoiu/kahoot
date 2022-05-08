package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.model.KahootQuestion
import java.sql.SQLException

class DbAllQuestions(private val db: DatabaseInterface.Executor) : DbExecutor.Get<List<KahootQuestion>> {
    override fun execute(id: Long): List<KahootQuestion> {
        val result = mutableListOf<KahootQuestion>()
        val sql = "SELECT id, kid, title, correct FROM Question WHERE kid=$id"
        val set = db.executeQuery(sql)
        try {
            while (set.next()) {
                val kid = set.getLong("id")
                val title = set.getString("title")
                val correct = set.getString("correct")
                val answers = DbAllAnswers(db).execute(kid)
                result.add(KahootQuestion(title, answers.toMutableList(), correct, kid))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return result
    }
}