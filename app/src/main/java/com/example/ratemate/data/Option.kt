package com.example.ratemate.data

data class Option(
    var optionId: String = "",
    var text: String = "",
    var order: Int = 0
) {
    constructor() : this("", "", 0)
}
