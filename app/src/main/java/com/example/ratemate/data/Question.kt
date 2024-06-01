package com.example.ratemate.data

data class Question(
    var questionId: String = "",
    var content: String = "",
    var options: List<String> = listOf(),
    var order: Int = 0
) {
    constructor() : this("", "", listOf(), 0)
}
