package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.model.Kahoot

class DbAddKahoot(private val db: DatabaseInterface.Executor) : DbExecutor.Base<Kahoot, Long> {
    override fun execute(objects: Kahoot): Long {
        val id: Long = if (objects.id == -1L) {
            val sql = "INSERT or IGNORE INTO Kahoot (title) VALUES (?)"
            db.executeUpdate(sql, objects.title)
        } else {
            val sql = "INSERT or IGNORE INTO Kahoot (id,title) VALUES (${objects.id}, ? )"
            db.execute(sql, objects.title)
            objects.id
        }
        val set = db.executeQuery("SELECT id FROM Question WHERE kid = $id")
        try {
            while (set.next()) {
                val qid = set.getLong("id")
                db.execute("DELETE FROM Answer WHERE qid = $qid")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        db.execute("DELETE FROM Question WHERE kid = $id")
        objects.questions.forEach { question ->
            val sqlQuestion = "INSERT or IGNORE INTO Question (kid,title,correct) VALUES (? , ? , ?)"
            val qId = db.executeUpdate(sqlQuestion, id, question.question, question.correct)
            question.answers.forEach { answer ->
                val sqlAnswer = "INSERT or IGNORE INTO Answer (qId,text) VALUES (?,?)"
                db.execute(sqlAnswer, qId, answer)
            }
        }
        return id
    }
}