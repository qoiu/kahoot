package com.example.kahoot.domain.clean

import com.example.kahoot.domain.model.Kahoot
import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User

interface AllRepository {
    fun kahoot(): Repository<Kahoot>
    fun user(): Repository<User>
    fun chat(): Repository<Reply>

    class Base(
        private val kahoot: Repository<Kahoot>,
        private val user: Repository<User>,
        private val chat: Repository<Reply>,
    ) : AllRepository {
        override fun kahoot(): Repository<Kahoot> = kahoot

        override fun user(): Repository<User> = user

        override fun chat(): Repository<Reply> = chat
    }
}