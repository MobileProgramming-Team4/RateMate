package com.example.ratemate.data

data class User(
    var userId: String = "",
    var email: String = "",
    var points: Int = 0,
    var createdDate: String = "",
    var modifiedDate: String = "",
    var status: String = "",
    var profileImage: String = "",
    var surveysCreated: List<String> = listOf(),
    var surveysParticipated: List<String> = listOf()
) {
    constructor() : this("", "", 0, "", "", "", "", listOf(), listOf())
}
