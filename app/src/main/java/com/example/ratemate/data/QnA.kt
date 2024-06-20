package com.example.ratemate.data

data class QnA(
    var order : Int = 0, //몇번째 질문인지
    var question : String = "", //질문
    var answerList : List<String> = mutableListOf(), //질문에 대한 답변 리스트
    var answerCountList : List<Int> = mutableListOf(), //유저들이 선택한 답변의 수
    var questionType : String = "" //radiobutton: "single", checkbox: "multiple"
){
    constructor() : this(0, "", mutableListOf(), mutableListOf(), "")
}

/***
 * 예시 데이터
 *
 * 질문 id : "1"
 * 질문 : "어떤 색깔을 좋아하시나요?"
 * 답변 리스트 : ["빨강", "파랑", "노랑", "초록"]
 * 답변 선택 수 : [3, 2, 1, 4]
 * 질문 타입 : "single"
 *
 *
 */