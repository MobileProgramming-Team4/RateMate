package com.example.ratemate.data

data class Comment(
    var commentId: String = "",
    var userId: String = "",
    var text: String = "",
    var createdDate: String = ""
) {
    constructor() : this("", "", "", "")
}
