package com.example.ratemate.home

import androidx.compose.ui.graphics.painter.Painter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Date
import java.util.UUID

data class SurveyResult(
    val title: String,
    val writer: String,
    val like: Int,
    val date: Date = Date(),
    val comments: List<Comment> = mutableListOf(),
    var surveyResultUserList: MutableList<SurveyResultUser>

)

data class Comment(
    val id: UUID = UUID.randomUUID(),
    var img: Painter,
    var username: String,
    var commentText: String,
    var like: Int,
    var dislike: Int,
    val date: Date = Date(),
    var commentUserList: MutableList<CommentUser>
)


data class CommentUser(
    val user: String,
    var isUsersComment: Boolean = false,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false
)


data class SurveyResultUser(
    val user: String,
    var isLiked: Boolean = false
)

data class User(
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser,
    val userImg: Painter,
    val userName: String
)

data class ResultContent(
    val question: String,
    val answer : List<String>,
    val answerCount : List<Int>
)