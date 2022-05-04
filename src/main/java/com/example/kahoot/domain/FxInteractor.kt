package com.example.kahoot.domain

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.data.executors.DbAddKahoot
import com.example.kahoot.data.executors.DbAllKahoot
import com.example.kahoot.domain.model.Kahoot
import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User
import com.example.kahoot.domain.presesnter.MainPresenter

interface FxInteractor : SimpleObserver<MsgReceiver> {
    fun getAllKahoot(): List<Kahoot>
    fun saveKahoot(kahoot: Kahoot)
    fun sendMsg(reply: Reply)
    fun cheatConnection(): List<User>
    fun close()

    class Base(private val db: DatabaseInterface.Executor, private val presenter: MainPresenter.Full) :
        BaseObserver<MsgReceiver>(), FxInteractor, MsgReceiver {

        init {
            presenter.subscribe(this)
        }

        override fun getAllKahoot(): List<Kahoot> = DbAllKahoot(db).execute()
        override fun saveKahoot(kahoot: Kahoot) {
            DbAddKahoot(db).execute(kahoot)
        }

        override fun sendMsg(reply: Reply) {
            presenter.postMsg(reply)
        }

        override fun close() {
            presenter.stopBot()
        }

        override fun cheatConnection(): List<User> {
            val users = presenter.getAllUsers()
            val result = mutableListOf<User>()
            users.forEach { user ->
                if (user.id == 794341050L) {
                    result.add(user)
//                    user.currentState = Ready()
//                    user.currentState.execute()
                }
            }
            return result
        }

        override fun receiveMsg(user: User, message: String) {
            receiverList.forEach { it.receiveMsg(user, message) }
        }
    }
}