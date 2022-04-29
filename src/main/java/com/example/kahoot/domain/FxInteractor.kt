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

        override fun receiveMsg(user: User, message: String) {
            receiverList.forEach { it.receiveMsg(user, message) }
        }
    }
}