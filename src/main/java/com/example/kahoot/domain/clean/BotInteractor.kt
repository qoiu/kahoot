package com.example.kahoot.domain.clean

import com.example.kahoot.domain.MsgReceiver
import com.example.kahoot.domain.SimpleObserver
import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User


interface BotInteractor : SimpleObserver<MsgReceiver>, DefineMainInteractor {
    /**
     * try to add user
     * return false if user exist
     */
    fun addUser(user: User): User
    fun getAllUsers(): Set<User>
    fun getUserById(id: Long): User
    fun getMsg(user: User, message: String)
    fun postMsg(reply: Reply)
    fun stopBot()

    class Base(private val bot: BotPresenter) : BaseInteractor(), BotInteractor {
        private val users: MutableSet<User> = mutableSetOf()

        override fun addUser(user: User): User {
            users.add(user)
            if (interactor.repository().user().read(Conditions.Base().addCondition("id", user.id)).isEmpty()) {
                interactor.repository().user().save(user)
                bot.sendMsg(Reply(user.id, "Hi"))
            }
            return user
        }

        override fun getAllUsers(): Set<User> = interactor.repository().user().read().toSet()

        override fun getUserById(id: Long): User {
            val result = interactor.repository().user().read(Conditions.Base().addCondition("id", id))
            if (result.isEmpty()) throw java.lang.IllegalStateException("NoUser")
            return result[0]
        }

        override fun getMsg(user: User, message: String) {
            interactor.repository().chat().save(Reply(user.id, message))
            user.execute(message)
            receiverList.forEach { it.receiveMsg(user, message) }
        }

        override fun postMsg(reply: Reply) {
            bot.sendMsg(reply)
        }

        override fun stopBot() {
            bot.stopBot()
        }
    }
}