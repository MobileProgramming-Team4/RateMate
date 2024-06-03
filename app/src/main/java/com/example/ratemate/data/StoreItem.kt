package com.example.ratemate.data

data class StoreItem(
    var itemId: String = "",
    var itemName: String = "",
    var cost: Int = 0,
    var description: String = ""
) {
    constructor() : this("", "", 0, "")
}
