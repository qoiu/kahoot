package com.example.kahoot.bot

import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User
import com.example.kahoot.domain.presesnter.MainPresenter
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

interface Bot {

    fun setPresenter(presenter: MainPresenter.Full)
    fun sendMsg(reply: Reply)
    fun clearChat(id: Long, user: Boolean = true, bot: Boolean = true)

    fun stop()

    interface Delete {
        fun deleteMsg(message: Message): Boolean
    }


    class Base(token: String, options: DefaultBotOptions = DefaultBotOptions()) : BaseBot.Base(token, options), Bot,
        Delete {

        private lateinit var presenter: MainPresenter.Full
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

        private fun getUser(tgUser: org.telegram.telegrambots.meta.api.objects.User): User = presenter.getUser(
            User(
                tgUser.id,
                tgUser.userName ?: " ",
                tgUser.userName ?: " ",
                tgUser.firstName
            )
        )

        override fun setPresenter(presenter: MainPresenter.Full) {
            this.presenter = presenter
            presenter.getAllUsers().forEach {
                chatBotHistory[it.id] = UserHistory()
            }
        }

        override fun sendMsg(reply: Reply) {
            val msg = ReplyToMessageMapper().map(reply)
            val message = execute(msg)
            chatBotHistory[reply.userID]?.saveMsg(message)
            clearMsgFromBotBeforeMessage(message)
        }

        override fun clearChat(id: Long, user: Boolean, bot: Boolean) {
            if (bot)
                chatBotHistory[id]?.clearHistory(this)
        }

        override fun stop() {
            onClosing()
        }

        private fun clearMsgFromBotBeforeMessage(message: Message) {
            for (i in 1..10) {
                if (!deleteMsg(message.chatId.toString(), message.messageId - i))
                    break
            }
        }
    }
}