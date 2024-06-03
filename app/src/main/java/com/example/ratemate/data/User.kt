package com.example.ratemate.data

data class User(
    var userId: String = "",
    var email: String = "",
    var points: Int = 0,
    var createdDate: String = "",
    var modifiedDate: String = "",
    var status: String = "",
    var profileImage: String = "",
    var surveysCreated: List<String> = mutableListOf(),
    var surveysParticipated: List<String> = mutableListOf(),
    var PurchaseList : List<StoreItem> = mutableListOf()
) {
    constructor() : this("", "", 0, "", "", "", "", mutableListOf(), mutableListOf(), mutableListOf())
}
