package com.example.kahoot.domain.model

data class KahootQuestion(
    var question: String,
    var answers: MutableList<String> = mutableListOf(),
    var correct: String = ""
) {

    override fun toString(): String = "$question\n${answers} \n Correct answer: $correct"
}