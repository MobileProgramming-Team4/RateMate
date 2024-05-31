package com.example.ratemate.data

data class Survey(
    val surveyId: String = "",
    val creatorId: String = "",
    val title: String = "",
    val content: String = "",
    val likes : Int = 0,
    val responses : Int = 0,
    val createdDate: String = "",
    val modifiedDate: String = "",
    val status: String = "",
    val questions: Map<String, Question> = emptyMap()
) {
    constructor() : this("", "", "", "", 0,0,"", "", "", emptyMap())
}
