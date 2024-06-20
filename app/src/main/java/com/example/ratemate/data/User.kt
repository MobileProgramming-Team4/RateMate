package com.example.ratemate.data

import com.example.ratemate.R

data class User(
    var userId: String = "",
    var email: String = "",
    var password: String = "",
    var points: Int = 0,
    var createdDate: String = "",
    var modifiedDate: String = "",
    var status: String = "",
    var profileImage: Int = R.drawable.profile,
    var surveysCreated: List<String> = mutableListOf(),
    var surveysParticipated: List<String> = mutableListOf(),
    var PurchaseList : List<StoreItem> = mutableListOf()
) {
    constructor() : this("", "", "", 0, "", "", "", R.drawable.profile, mutableListOf(), mutableListOf(), mutableListOf())
}
