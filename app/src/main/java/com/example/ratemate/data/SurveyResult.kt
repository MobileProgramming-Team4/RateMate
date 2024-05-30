package com.example.ratemate.data

data class SurveyResult(
    val surveyId: String = "",
    val responses: Map<String, Response> = emptyMap(),
    val likes: Like = Like(),
    val comments: Map<String, Comment> = emptyMap()
) {
    constructor() : this("", emptyMap(), Like(), emptyMap())
}
