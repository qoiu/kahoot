package com.example.kahoot.domain.clean

import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User

interface BotPresenter {
    fun setInteractor(interactor: BotInteractor)
    fun lostConnection()
    fun stopBot()
    fun addUser(user: User): User
    fun getMsg(user: User, message: String)
    fun getAllUsers(): Set<User>
    fun getUser(user: User): User
    fun sendMsg(reply: Reply)
}