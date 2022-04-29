package com.example.kahoot.bot

import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

interface BaseBot {
    fun deleteMsg(message: Message): Boolean

    abstract class Base(private val token: String, options: DefaultBotOptions = DefaultBotOptions()) :
        TelegramLongPollingBot(options), BaseBot {

        override fun deleteMsg(message: Message): Boolean {
            return deleteMsg(message.chatId.toString(), message.messageId)
        }

        fun deleteMsg(id: String, messageId: Int): Boolean {
            val delete = DeleteMessage()
            delete.chatId = id
            delete.messageId = messageId
            try {
                execute(delete)
            } catch (e: TelegramApiException) {
                return false
            }
            return true
        }

        override fun getBotToken(): String = token
    }
}