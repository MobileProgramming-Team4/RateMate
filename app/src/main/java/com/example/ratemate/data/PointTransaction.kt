package com.example.ratemate.data

data class PointTransaction(
    var transactionId: String = "",
    var userId: String = "",
    var amount: Int = 0,
    var transactionType: String = "",
    var transactionDate: String = "",
    var itemId: String? = null
) {
    constructor() : this("", "", 0, "", "", null)
}
