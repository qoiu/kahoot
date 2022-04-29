package com.example.kahoot.bot

import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User
import com.example.kahoot.domain.model.UserState
import com.example.kahoot.domain.presesnter.MainPresenter
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

interface Bot {

    fun setPresenter(presenter: MainPresenter)
    fun sendMsg(reply: Reply)
    fun clearChat(id: Long, user: Boolean = true, bot: Boolean = true)

    fun stop()

    interface Delete {
        fun deleteMsg(message: Message): Boolean
    }


    class Base(token: String, options: DefaultBotOptions = DefaultBotOptions()) : BaseBot.Base(token, options), Bot, Delete {

        private lateinit var presenter: MainPresenter
        private val users: MutableSet<User> = mutableSetOf()
        private val chatBotHistory = mutableMapOf<Long, UserHistory>()

        override fun getBotUsername(): String = "@TestBot"


        override fun onUpdateReceived(update: Update?) {
            println("getMsg: " + update?.message?.text)
            if (update != null) {
                if (update.hasMessage()) {
                    val message = update.message
                    val tgUser = message.from
                    val user = getUser(tgUser)
                    update.message.chatId
                    if (tgUser.firstName.isNotEmpty() || tgUser.lastName.isNotEmpty())
                        "${tgUser.firstName} ${tgUser.lastName}"
                    presenter.getMsg(user, message.text)
                } else if (update.hasCallbackQuery()) {
                    val message = update.callbackQuery
                    val tgUser = message.from
                    val user = getUser(tgUser)
                    presenter.getMsg(user, update.callbackQuery.data)

                } else {
                    println("else")
                }
            }
            update?.let { deleteMsg(it.message) }
        }

        private fun getUser(tgUser: org.telegram.telegrambots.meta.api.objects.User): User {
            val user = users.find { it.id == tgUser.id }
            return if (user != null) {
                user
            } else {
                val newUser = User(tgUser.id, tgUser.userName?:" ", tgUser.userName?:" ", tgUser.firstName?:" ", UserState.Default())
                presenter.addUser(newUser)
                newUser
            }
        }

        override fun setPresenter(presenter: MainPresenter) {
            this.presenter = presenter
            val extra = presenter.getAllUsers()
            users.addAll(extra)
            users.forEach {
                chatBotHistory[it.id] = UserHistory()
            }
        }

        override fun sendMsg(reply: Reply) {
            val msg = ReplyToMessageMapper().map(reply)
            val message = execute(msg)
            chatBotHistory[reply.userID]?.saveMsg(message)
        }

        override fun clearChat(id: Long, user: Boolean, bot: Boolean) {
            if (bot)
                chatBotHistory[id]?.clearHistory(this)
        }

        override fun stop() {
            onClosing()
        }

        private fun clearMsgFromBotBeforeMessage(message: Message) {
            for (i in 1..100) {
//                deleteMsg(message.chatId.toString(), message.messageId - i)
                if (!deleteMsg(message.chatId.toString(), message.messageId - i))
                    break
            }
        }
    }
}