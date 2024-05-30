package com.example.ratemate.data

data class PointTransaction(
    val transactionId: String = "",
    val userId: String = "",
    val amount: Int = 0,
    val transactionType: String = "",
    val transactionDate: String = "",
    val itemId: String? = null
) {
    constructor() : this("", "", 0, "", "", null)
}
