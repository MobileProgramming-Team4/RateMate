package com.example.ratemate.data

data class StoreItem(
    val itemId: String = "",
    val itemName: String = "",
    val cost: Int = 0,
    val description: String = ""
) {
    constructor() : this("", "", 0, "")
}
