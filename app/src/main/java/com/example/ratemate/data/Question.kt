package com.example.ratemate.data

data class Question(
    var questionId: String = "",
    var content: String = "",
    var options: MutableList<Option> = mutableListOf(),
    var order: Int = 0
) {
    constructor() : this("", "", mutableListOf(), 0)
}
