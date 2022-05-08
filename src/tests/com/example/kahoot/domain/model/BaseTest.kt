package com.example.kahoot.domain.model

import com.example.kahoot.domain.clean.MainInteractor
import org.junit.jupiter.api.BeforeEach

abstract class BaseTest {

    open val interactor = MainInteractor.Test()

    @BeforeEach
    open fun setUp() {
        UserState.init(interactor)
    }
}