package com.example.kahoot.domain

import com.example.kahoot.domain.model.User

interface MsgReceiver {
    fun receiveMsg(user: User, message: String)
}