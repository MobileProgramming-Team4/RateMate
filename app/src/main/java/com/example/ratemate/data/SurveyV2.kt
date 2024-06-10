package com.example.ratemate.data

data class SurveyV2(
    var surveyId: String = "",
    var creatorId: String = "",
    var title: String = "",
    var content: String = "",
    var likes: Like = Like(),
    var numOfComments: Int = 0,
    var createdDate: String = "",
    var modifiedDate: String = "",
    var status: String = "",
    var qnA:  MutableList<QnA> = mutableListOf(),
    var response: MutableList<Response> = mutableListOf(),
    var comments: MutableList<Comment> = mutableListOf()
) {
    constructor() : this("", "", "", "", Like(), 0, "", "", "", mutableListOf(), mutableListOf(), mutableListOf())

}

/***
 * 예시 데이터
 *
 * 설문 id : "1"
 * 유저 id : "1"
 * 제목 : "좋아하는 색깔"
 * 내용 : "어떤 색깔을 좋아하시나요?"
 * 좋아요 수 : 3
 * 댓글 수 : 2
 * 생성 날짜 : "2021-06-01"
 * 수정 날짜 : "2021-06-02"
 * 상태 : "active"
 * 질문 리스트 (qnA) : [
 * {
 *    "order" : 1,
 *    "question" : "어떤 색깔을 좋아하시나요?",
 *    "answerList" : ["빨강", "파랑", "노랑", "초록"],
 *    "answerCountList" : [3, 2, 1, 4],
 *    "questionType" : "single"
 *    },
 *    {
 *    "order" : 2,
 *    "question" : "어떤 색깔을 싫어하시나요?",
 *     "answerList" : ["빨강", "파랑", "노랑", "초록"],
 *     "answerCountList" : [3, 2, 1, 4],
 *     "questionType" : "multiple"
 *     }
 *     ]
 * 댓글 리스트 : [
 * {
 *   "commentId" : "1",
 *   "userId" : "1",
 *   "text" : "좋아하는 색깔은 빨강이에요",
 *   "createdDate" : "2021-06-01",
 *   "modifiedDate" : "2021-06-01"
 *   },
 *   {
 *   "commentId" : "2",
 *   "userId" : "2",
 *   "text" : "저는 파랑을 좋아해요",
 *   "createdDate" : "2021-06-01",
 *   "modifiedDate" : "2021-06-01"
 *   }
 *   ]
 */