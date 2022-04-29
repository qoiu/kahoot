package com.example.kahoot.bot

import org.telegram.telegrambots.meta.api.objects.Message

class UserHistory {
    private val history = mutableListOf<Message>()

    fun saveMsg(message: Message) {
        history.add(message)
    }

    fun clearHistory(bot: Bot.Delete) {
        history.forEach {
            bot.deleteMsg(it)
        }
        history.clear()
    }
}