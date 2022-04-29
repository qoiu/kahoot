package com.example.kahoot.data.repository

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.data.executors.DbAddKahoot
import com.example.kahoot.data.executors.DbAllKahoot
import com.example.kahoot.domain.Repository
import com.example.kahoot.domain.model.Kahoot

class KahootRepository(private val db: DatabaseInterface.Executor) : Repository<Kahoot> {
    override fun save(data: Kahoot) {
        DbAddKahoot(db).execute(data)
    }

    override fun read(): List<Kahoot> = DbAllKahoot(db).execute()
}