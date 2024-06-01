package com.example.ratemate.data

data class Dislike(
    var count: Int = 0,
    var usersWhoDisliked: MutableList<String> = mutableListOf()
) {
    constructor() : this(0, mutableListOf())
}
