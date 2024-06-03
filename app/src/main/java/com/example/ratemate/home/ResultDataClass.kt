package com.example.ratemate.home

import androidx.compose.ui.graphics.painter.Painter
import com.example.ratemate.data.Comment
import com.example.ratemate.data.Like
import com.example.ratemate.data.Question
import com.example.ratemate.data.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Date
import java.util.UUID

data class SurveyResultEX(
    var surveyId: String = "",
    var creatorId: String = "",
    var title: String = "",
    var content: String = "",
    var likes: Like = Like(),
    var createdDate: String = "",
    var modifiedDate: String = "",
    var status: String = "",
    var questions: Map<String, Question> = emptyMap(),
    var responses: Map<String, Response> = emptyMap(),
    var comments: Map<String, Comment> = emptyMap()
) {
    constructor() : this("", "", "", "", Like(), "", "", "", emptyMap(), emptyMap(), emptyMap())

}


data class ResultContent(
    val question: String,
    val answer : List<String>,
    val answerCount : List<Int>
)