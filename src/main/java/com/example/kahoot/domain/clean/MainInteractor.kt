package com.example.kahoot.domain.clean

import com.example.kahoot.domain.model.UserState

interface MainInteractor {
    fun repository(): AllRepository
    fun bot(): BotInteractor

    class Base(
        private val fxInteractor: FxInteractor,
        private val bot: BotInteractor,
        private val repository: AllRepository
    ) : MainInteractor {
        init {
            UserState.init(this)
        }

        override fun repository(): AllRepository = repository
        override fun bot(): BotInteractor = bot

    }

    class Test : MainInteractor {
        override fun repository(): AllRepository {
            TODO("Not yet implemented")
        }

        override fun bot(): BotInteractor {
            TODO("Not yet implemented")
        }

    }
}