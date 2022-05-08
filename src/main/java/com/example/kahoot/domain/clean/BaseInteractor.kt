package com.example.kahoot.domain.clean

import com.example.kahoot.domain.BaseObserver
import com.example.kahoot.domain.MsgReceiver

abstract class BaseInteractor : BaseObserver<MsgReceiver>(), DefineMainInteractor {
    protected lateinit var interactor: MainInteractor

    override fun setMainInteractor(mainInteractor: MainInteractor) {
        this.interactor = mainInteractor
    }
}