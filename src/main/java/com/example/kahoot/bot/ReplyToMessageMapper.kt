package com.example.kahoot.bot

import com.example.kahoot.Mapper
import com.example.kahoot.domain.model.Reply
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class ReplyToMessageMapper : Mapper.Base<Reply, SendMessage> {
    override fun map(data: Reply): SendMessage {
        val columns = mutableListOf(listOf<InlineKeyboardButton>())
        data.btns.forEach {
            if (it.text != "")
                columns.add(
                    listOf(
                        InlineKeyboardButton.builder()
                            .text(it.text)
                            .callbackData(it.command)
                            .build()
                    )
                )
        }
        val markup = InlineKeyboardMarkup()
        markup.keyboard = columns
        val msg = SendMessage.builder()
            .chatId(data.userID.toString())
            .text(data.text)
            .replyMarkup(markup)
            .build()
        msg.enableHtml(true)
        return msg
    }
}