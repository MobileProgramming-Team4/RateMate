package com.example.ratemate.data

data class Response(
    var userId: String = "",
    var answer : List<List<Int>>//몇번 질문을 골랐는지
) {
    constructor() : this("", mutableListOf())
}

/***
 *
 * 예시 데이터
 *
 * 유저 id : "1"
 * 답변 : [[1, 3], [0], [2, 3]]
 *
 *
 */