package com.example.kahoot.domain.presesnter

import com.example.kahoot.bot.Bot
import com.example.kahoot.data.DatabaseBase
import com.example.kahoot.data.executors.DbAddMessage
import com.example.kahoot.data.executors.DbAddUser
import com.example.kahoot.data.executors.DbAllUsers
import com.example.kahoot.domain.BaseObserver
import com.example.kahoot.domain.MsgReceiver
import com.example.kahoot.domain.SimpleObserver
import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User
import com.example.kahoot.domain.model.UserState

interface MainPresenter : SimpleObserver<MsgReceiver> {

    interface MessengerActions{
        fun getMsg(user: User, message: String)
        fun postMsg(message: Reply, clear: Boolean = true)

        class Test : MessengerActions{
            private var message: String=""
            private var reply: Reply = Reply(0,"empty")

            val actualMsg: String
            get() = message

            val actualReply: Reply
            get() = reply
            override fun getMsg(user: User, message: String) {
                this.message = message
            }

            override fun postMsg(message: Reply, clear: Boolean) {
                this.reply = message
            }
        }
    }

    interface BotActions: UserActions{
        fun setBot(bot: Bot)
        fun lostConnection()
        fun stopBot()
        fun addUser(user: User): User
    }

    interface UserActions{
        fun getAllUsers(): Set<User>
        fun getUser(user: User): User
    }

    interface Full: MessengerActions, BotActions, MainPresenter

    class Base(private val db: DatabaseBase) : BaseObserver<MsgReceiver>(), Full {


        private val users: MutableSet<User> = mutableSetOf()
        init {
            UserState.init(db, this)
            users.addAll(DbAllUsers(db).execute())
        }

        private lateinit var bot: Bot
        override fun setBot(bot: Bot) {
            this.bot = bot
            bot.setPresenter(this)
        }

        override fun lostConnection() {
        }

        override fun getAllUsers(): Set<User> = users

        override fun getUser(user: User): User {
            val result = users.find { it.id == user.id }
            return result ?: addUser(user)
        }

        override fun addUser(user: User): User {
            users.add(user)
            if (DbAddUser(db).execute(user) > 0) {
                bot.sendMsg(Reply(user.id, "Hi"))
            }
            return user
        }

        override fun getMsg(user: User, message: String) {
            DbAddMessage(db).execute(Reply(user.id,message))
            user.execute(message)
            receiverList.forEach { it.receiveMsg(user, message) }
        }

        override fun postMsg(message: Reply, clear: Boolean) {
            if (clear)
                bot.clearChat(message.userID)
            bot.sendMsg(message)
        }

        override fun stopBot() {
            bot.stop()
        }
    }
}