package com.example.kahoot.domain.fx

import com.example.kahoot.data.DatabaseInterface

interface FxPresenter {
    fun init(db: DatabaseInterface.Executor, presenter: FxPresenter, debug: Boolean)
    fun create(clazz: Class<FxPresenter>): FxPresenter
    fun close()

    abstract class Base : FxPresenter {
        private lateinit var db: DatabaseInterface.Executor
        private lateinit var presenter: FxPresenter
        private var debugMode: Boolean = false

        final override fun create(clazz: Class<FxPresenter>): FxPresenter {
            val interactor = clazz.newInstance()
            interactor.init(db, presenter, debugMode)
            return interactor
        }

        final override fun init(db: DatabaseInterface.Executor, presenter: FxPresenter, debug: Boolean) {
            this.db = db
            this.presenter = presenter
            this.debugMode = debug
        }
    }
}