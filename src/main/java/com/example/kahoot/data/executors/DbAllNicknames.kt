package com.example.kahoot.data.executors

import com.example.kahoot.data.DatabaseInterface
import java.sql.SQLException

class DbAllNicknames(private val db: DatabaseInterface.Executor) : DbExecutor.Base<Long, List<String>> {

    override fun execute(objects: Long): List<String> {
        val result = mutableListOf<String>()
        val sql = "SELECT nick FROM Nicknames WHERE id = $objects"
        val set = db.executeQuery(sql)
        try {
            while (set.next()) {
                result.add(set.getString("nick"))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return result
    }
}