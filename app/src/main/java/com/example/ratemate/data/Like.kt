package com.example.ratemate.data

data class Like(
    var count: Int = 0,
    var usersWhoLiked: MutableList<String> = mutableListOf()
) {
    constructor() : this(0, mutableListOf())
}