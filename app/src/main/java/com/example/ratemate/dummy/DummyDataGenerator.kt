package com.example.ratemate.dummy

import android.util.Log
import com.example.ratemate.data.*
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DummyDataGeneratorV2 {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("surveysV2")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun generateDummyDataV2() {
        val surveys = mutableListOf(
            SurveyV2(
                surveyId = "106",
                creatorId = "creator106",
                title = "Favorite Streaming Services Survey",
                content = "Which streaming services do you use the most and why?",
                likes = Like(count = 20),
                numOfComments = 5,
                createdDate = dateFormat.format(Date()),
                modifiedDate = dateFormat.format(Date()),
                status = "Active",
                qnA = mutableListOf(
                    QnA(order = 1, question = "What is your most used streaming service?", answerList = listOf("Netflix", "Amazon Prime", "Hulu", "Disney+", "Other"), answerCountList = listOf(10, 5, 3, 2, 1), questionType = "single")
                ),
                response = mutableListOf(
                    Response(userId = "user201", answer = listOf(listOf(0))),
                    Response(userId = "user202", answer = listOf(listOf(1))),
                    Response(userId = "user203", answer = listOf(listOf(2))),
                    Response(userId = "user204", answer = listOf(listOf(3))),
                    Response(userId = "user205", answer = listOf(listOf(4)))
                ),
                comments = mutableListOf(
                    Comment(commentId = "comment106", userId = "user206", text = "Netflix has the best shows!", createdDate = dateFormat.format(Date()), profileImage = "image_url106", like = Like(count = 3), dislike = Dislike(count = 1))
                )
            ),
            SurveyV2(
                surveyId = "107",
                creatorId = "creator107",
                title = "Coffee Consumption Habits",
                content = "How often do you consume coffee and what types do you prefer?",
                likes = Like(count = 25),
                numOfComments = 6,
                createdDate = dateFormat.format(Date()),
                modifiedDate = dateFormat.format(Date()),
                status = "Active",
                qnA = mutableListOf(
                    QnA(order = 1, question = "How many cups of coffee do you drink each day?", answerList = listOf("1 cup", "2 cups", "3 cups", "4 cups", "5 or more cups"), answerCountList = listOf(5, 10, 15, 8, 2), questionType = "single")
                ),
                response = mutableListOf(
                    Response(userId = "user301", answer = listOf(listOf(0))),
                    Response(userId = "user302", answer = listOf(listOf(1))),
                    Response(userId = "user303", answer = listOf(listOf(2))),
                    Response(userId = "user304", answer = listOf(listOf(3))),
                    Response(userId = "user305", answer = listOf(listOf(4))),
                    Response(userId = "user306", answer = listOf(listOf(4))),
                    Response(userId = "user307", answer = listOf(listOf(4))),
                    Response(userId = "user308", answer = listOf(listOf(4))),
                    Response(userId = "user309", answer = listOf(listOf(4))),
                    Response(userId = "user310", answer = listOf(listOf(4)))
                ),
                comments = mutableListOf(
                    Comment(commentId = "comment107", userId = "user311", text = "I can't start my day without coffee!", createdDate = dateFormat.format(Date()), profileImage = "image_url107", like = Like(count = 5), dislike = Dislike(count = 2))
                )
            )
        )

        // Sequentially add each survey to Firebase
        surveys.forEach { survey ->
            addSurveyToFirebase(survey)
        }
    }

    private fun addSurveyToFirebase(survey: SurveyV2) {
        dbRef.push().setValue(survey).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("DummyDataGeneratorV2", "Survey successfully added to Firebase with ID: ${survey.surveyId}")
            } else {
                Log.e("DummyDataGeneratorV2", "Failed to add survey to Firebase: ${task.exception?.message}")
            }
        }
    }
}
