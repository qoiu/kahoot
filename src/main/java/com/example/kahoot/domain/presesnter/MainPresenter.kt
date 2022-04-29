package com.example.kahoot.domain.presesnter

import com.example.kahoot.bot.Bot
import com.example.kahoot.data.DatabaseBase
import com.example.kahoot.data.executors.DbAddUser
import com.example.kahoot.data.executors.DbAllUsers
import com.example.kahoot.domain.BaseObserver
import com.example.kahoot.domain.MsgReceiver
import com.example.kahoot.domain.SimpleObserver
import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User
import com.example.kahoot.domain.model.UserState

interface MainPresenter : SimpleObserver<MsgReceiver> {

    fun setBot(bot: Bot)
    fun lostConnection()
    fun getAllUsers(): Set<User>
    fun addUser(user: User)
    fun getMsg(user: User, message: String)
    fun postMsg(message: Reply, clear: Boolean = true)
    fun stopBot()

    class Base(private val db: DatabaseBase) : BaseObserver<MsgReceiver>(), MainPresenter {

        init {
            UserState.init(db, this)
        }

        private lateinit var getMsg: (User, String) -> Unit
        private lateinit var bot: Bot
        override fun setBot(bot: Bot) {
            this.bot = bot
            bot.setPresenter(this)
        }

        override fun lostConnection() {
//            DbMapperRestartApp(db).map(null)
//            for (id in tables.getPlayersId()) {
//                if (tables.getGame(id) != null) {
//                    tables.getGame(id).playerLeaveGame(UserMessaged(id, "somePlayer"))
//                    tables.setGameEngine(id, null)
//                }
//            }
//            tables.clearTables()
        }

        override fun getAllUsers(): Set<User> = DbAllUsers(db).execute()

        override fun addUser(user: User) {
            if (DbAddUser(db).execute(user) > 0) {
                bot.sendMsg(Reply(user.id, "Hi"))
            }
        }

        override fun getMsg(user: User, message: String) {
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