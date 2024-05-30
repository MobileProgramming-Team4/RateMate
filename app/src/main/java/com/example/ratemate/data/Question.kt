package com.example.ratemate.data

data class Question(
    val questionId: String = "",
    val content: String = "",
    val options: List<String> = listOf(),
    val order: Int = 0
) {
    constructor() : this("", "", listOf(), 0)
}
