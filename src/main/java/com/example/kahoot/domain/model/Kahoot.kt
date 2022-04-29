package com.example.kahoot.domain.model

data class Kahoot(
    val id: Long,
    val title: String,
    val questions: MutableList<KahootQuestion> = mutableListOf(),
)