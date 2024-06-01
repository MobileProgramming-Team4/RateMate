package com.example.ratemate.data

import androidx.compose.ui.graphics.painter.Painter

data class Comment(
    var commentId: String = "",
    var userId: String = "",
    var text: String = "",
    var createdDate: String = "",
    var profileImage: Painter? = null,
    var like: Like = Like(),
    var dislike : Dislike = Dislike()
) {
    constructor() : this("", "", "", "", null, Like() , Dislike())
}
