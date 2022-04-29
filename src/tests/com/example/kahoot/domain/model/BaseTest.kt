package com.example.kahoot.domain.model

import com.example.kahoot.data.DatabaseInterface
import com.example.kahoot.domain.presesnter.MainPresenter
import org.junit.jupiter.api.BeforeEach

abstract class BaseTest {

    open val presenter = MainPresenter.MessengerActions.Test()

    @BeforeEach
    open fun setUp() {
        UserState.init(DatabaseInterface.Executor.Test(), presenter)
    }
}