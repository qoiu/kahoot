package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.model.KahootQuestion

class DbAddQuestion(private val db: DatabaseInterface.Executor) : DbExecutor.Base<KahootQuestion, Int> {
    override fun execute(objects: KahootQuestion): Int {
        val sql = "INSERT or IGNORE INTO Question (title,correct) VALUES (? , ?)"
        val qId = db.executeUpdate(sql, objects.question, objects.correct)
        objects.answers.forEach {
            val answer = "INSERT or IGNORE INTO Answer (qId,text) VALUES (?,?)"
            db.execute(answer, qId, it)
        }
        return qId
    }
}