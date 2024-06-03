package com.example.ratemate.data

data class SurveyResult(
    var surveyId: String = "",
    var responses: Map<String, Response> = emptyMap(),
    var likes: Like = Like(),
    var comments: Map<String, Comment> = emptyMap()
) {
    constructor() : this("", emptyMap(), Like(), emptyMap())
}
