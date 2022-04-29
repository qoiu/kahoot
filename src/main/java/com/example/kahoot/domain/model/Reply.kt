package com.example.kahoot.domain.model

data class Reply(
    val userID: Long,
    val text: String,
    val btns: List<Btn> = emptyList()
)