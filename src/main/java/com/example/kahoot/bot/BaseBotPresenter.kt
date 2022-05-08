package com.example.kahoot.bot

import com.example.kahoot.domain.clean.BotInteractor
import com.example.kahoot.domain.clean.BotPresenter
import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User

class BaseBotPresenter(private val bot: Bot) : BotPresenter {

    private lateinit var botInteractor: BotInteractor

    init {
        bot.setPresenter(this)
    }

    override fun addUser(user: User): User = botInteractor.addUser(user)


    override fun stopBot() = bot.stop()
    override fun setInteractor(interactor: BotInteractor) {
        this.botInteractor = interactor
    }

    override fun lostConnection() {
    }


    override fun getMsg(user: User, message: String) = botInteractor.getMsg(user, message)

    override fun getAllUsers(): Set<User> = botInteractor.getAllUsers()

    override fun getUser(user: User): User = botInteractor.getUserById(user.id)

    override fun sendMsg(reply: Reply) = bot.sendMsg(reply)
}