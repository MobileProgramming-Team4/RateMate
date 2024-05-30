package com.example.ratemate.data

data class User(
    val userId: String = "",
    val email: String = "",
    val points: Int = 0,
    val createdDate: String = "",
    val modifiedDate: String = "",
    val status: String = "",
    val profileImage: String = "",
    val surveysCreated: List<String> = listOf(),
    val surveysParticipated: List<String> = listOf()
) {
    constructor() : this("", "", 0, "", "", "", "", listOf(), listOf())
}
