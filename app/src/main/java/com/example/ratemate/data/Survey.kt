package com.example.ratemate.data

data class Survey(
    var surveyId: String = "",
    var creatorId: String = "",
    var title: String = "",
    var content: String = "",
    var likes : Int = 0,
    var responses : Int = 0,
    var createdDate: String = "",
    var modifiedDate: String = "",
    var status: String = "",
    var questions: Map<String, Question> = emptyMap()
) {
    constructor() : this("", "", "", "", 0,0,"", "", "", emptyMap())
}
