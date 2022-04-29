package com.example.kahoot.domain.model

data class KahootSession(
    val user: User,
    val list: List<KahootQuestion>,
    val statistic: KahootStats,
    val pin: Int = (1000..9999).random()
)