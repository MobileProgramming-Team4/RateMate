package com.example.ratemate.data

data class Response(
    var userId: String = "",
    var selectedOption: String = "",
    var responseDate: String = ""
) {
    constructor() : this("", "", "")
}