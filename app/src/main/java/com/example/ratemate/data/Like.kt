package com.example.ratemate.data

data class Like(
    val count: Int = 0,
    val usersWhoLiked: List<String> = listOf()
) {
    constructor() : this(0, listOf())
}