package com.example.kahoot.domain.model

data class User(
    val id: Long,
    var currentNick: String,
    val nickTg: String = "",
    val nameTg: String = "",
    private val state: UserState = UserState.Default()
) {
    var currentState: UserState = UserState.Null
        set(value) {
            field = value
            field.initUser(this)
        }

    init {
        currentState = state
    }

    fun execute(message: String) {
        currentState.execute(message)
    }

    fun execute() {
        currentState.execute()
    }
}