package com.example.ratemate.data

data class SurveyV2(
    var surveyId: String = "",
    var creatorId: String = "",
    var title: String = "",
    var content: String = "",
    var likes: Like = Like(),
    var numOfComments: Int = 0,
    var createdDate: String = "",
    var modifiedDate: String = "",
    var status: String = "",
    var questions: MutableList<Question> = mutableListOf(),
    var responses: MutableList<Response> = mutableListOf(),
    var comments: MutableList<Comment> = mutableListOf()
) {
    constructor() : this("", "", "", "", Like(), 0, "", "", "", mutableListOf(), mutableListOf(), mutableListOf())

}
