package com.example.ratemate.home

import androidx.compose.ui.graphics.painter.Painter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Date
import java.util.UUID

data class SurveyResult(
    val title: String,
    val writer: String,
    val content: String,
    val like: Int,
    val date: Date = Date(),
    val comments: List<Comment> = mutableListOf(),
    var SurveyResult_User_List: MutableList<SurveyResult_User>

)

data class Comment(
    val id: UUID = UUID.randomUUID(),
    var img: Painter,
    var username: String,
    var commentText: String,
    var like: Int,
    var dislike: Int,
    val date: Date = Date(),
    var Comment_User_List: MutableList<Comment_User>
)


data class Comment_User(
    val user: String,
    var isUsersComment: Boolean = false,
    var isLiked: Boolean = false,
    var isDisliked: Boolean = false
)


data class SurveyResult_User(
    val user: String,
    var isLiked: Boolean = false
)

data class User(
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser,
    val userImg: Painter,
    val userName: String
)
