package com.example.kahoot.bot

import com.example.kahoot.domain.clean.BotPresenter
import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

interface Bot {

    fun setPresenter(presenter: BotPresenter)
    fun sendMsg(reply: Reply)
    fun clearChat(id: Long, user: Boolean = true, bot: Boolean = true)

    fun stop()

    interface Delete {
        fun deleteMsg(message: Message): Boolean
    }


    class Base(token: String, options: DefaultBotOptions = DefaultBotOptions()) : BaseBot.Base(token, options), Bot,
        Delete {
        private lateinit var presenter: BotPresenter

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

        override fun setPresenter(presenter: BotPresenter) {
            this.presenter = presenter
        }

        override fun sendMsg(reply: Reply) {
            val msg = ReplyToMessageMapper().map(reply)
            val message = execute(msg)
            clearMsgFromBotBeforeMessage(message)
        }

        override fun clearChat(id: Long, user: Boolean, bot: Boolean) {
            // TODO: 08.05.2022 clearChat?
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