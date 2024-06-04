package com.example.ratemate.dummy

import android.util.Log
import com.example.ratemate.data.Option
import com.example.ratemate.data.Question
import com.example.ratemate.data.Survey
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DummyDataGenerator {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("surveys")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun generateDummyData() {
        val survey1 = Survey(
            creatorId = "user1",
            title = "Favorite Food Survey",
            content = "Tell us about your favorite food.",
            likes = 10,
            responses = 5,
            createdDate = dateFormat.format(Date()),
            modifiedDate = dateFormat.format(Date()),
            status = "Active",
            questions = mutableListOf(
                Question(
                    content = "What is your favorite cuisine?",
                    options = mutableListOf(
                        Option(text = "Italian", order = 1),
                        Option(text = "Japanese", order = 2)
                    ),
                    order = 1
                ),
                Question(
                    content = "How often do you eat out?",
                    options = mutableListOf(
                        Option(text = "Daily", order = 1),
                        Option(text = "Weekly", order = 2),
                        Option(text = "Monthly", order = 3)
                    ),
                    order = 2
                )
            )
        )

        val survey2 = Survey(
            creatorId = "user2",
            title = "Exercise Habits Survey",
            content = "Tell us about your exercise habits.",
            likes = 20,
            responses = 10,
            createdDate = dateFormat.format(Date()),
            modifiedDate = dateFormat.format(Date()),
            status = "Active",
            questions = mutableListOf(
                Question(
                    content = "How often do you exercise?",
                    options = mutableListOf(
                        Option(text = "Daily", order = 1),
                        Option(text = "Weekly", order = 2),
                        Option(text = "Monthly", order = 3)
                    ),
                    order = 1
                ),
                Question(
                    content = "What is your favorite type of exercise?",
                    options = mutableListOf(
                        Option(text = "Running", order = 1),
                        Option(text = "Swimming", order = 2),
                        Option(text = "Cycling", order = 3)
                    ),
                    order = 2
                )
            )
        )

        addSurveyWithAutoIncrementId(survey1) {
            addSurveyWithAutoIncrementId(survey2)
        }
    }

    private fun addSurveyWithAutoIncrementId(survey: Survey, callback: (() -> Unit)? = null) {
        dbRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val maxId = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.maxOrNull() ?: 0
                val newSurveyId = (maxId + 1).toString()
                survey.surveyId = newSurveyId
                dbRef.child(newSurveyId).setValue(survey).addOnCompleteListener {
                    Log.d("DummyDataGenerator", "Survey added with ID: $newSurveyId")
                    callback?.invoke()
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.e("DummyDataGenerator", "Failed to fetch last survey id: ${error.message}")
            }
        })
    }
}
