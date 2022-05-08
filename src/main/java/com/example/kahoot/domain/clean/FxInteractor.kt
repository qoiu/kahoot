package com.example.kahoot.domain.clean

import com.example.kahoot.domain.MsgReceiver
import com.example.kahoot.domain.SimpleObserver
import com.example.kahoot.domain.model.Kahoot
import com.example.kahoot.domain.model.Reply
import com.example.kahoot.domain.model.User

interface FxInteractor : SimpleObserver<MsgReceiver>, DefineMainInteractor {
    fun getAllKahoot(): List<Kahoot>
    fun saveKahoot(kahoot: Kahoot)
    fun sendMsg(reply: Reply)
    fun cheatConnection(): List<User>
    fun isDebugVersion(): Boolean
    fun close()

    class Base(private val debugMode: Boolean = false) :
        BaseInteractor(), FxInteractor, MsgReceiver {

        override fun getAllKahoot(): List<Kahoot> = interactor.repository().kahoot().read()
        override fun saveKahoot(kahoot: Kahoot) {
            interactor.repository().kahoot().save(kahoot)
        }

        override fun sendMsg(reply: Reply) {
            interactor.bot().postMsg(reply)
        }

        override fun close() {
            interactor.bot().stopBot()
        }

        override fun cheatConnection(): List<User> =
            interactor.repository().user().read(Conditions.Base("id", 794341050L))

        override fun setMainInteractor(mainInteractor: MainInteractor) {
            super.setMainInteractor(mainInteractor)
            interactor.bot().subscribe(this)
        }

        override fun isDebugVersion(): Boolean = debugMode

        override fun receiveMsg(user: User, message: String) {
            receiverList.forEach { it.receiveMsg(user, message) }
        }
    }
}