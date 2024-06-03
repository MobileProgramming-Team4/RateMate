package com.example.ratemate.dummy

import com.example.ratemate.data.Survey
import com.example.ratemate.repository.SurveyRepository
import java.text.SimpleDateFormat
import java.util.*

fun addDummySurveys(repository: SurveyRepository) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = dateFormat.format(Date())

    val dummySurveys = listOf(
        Survey(
            surveyId = "",
            creatorId = "user1",
            title = "Favorite Food Survey",
            content = "Tell us about your favorite foods.",
            likes = 10,
            responses = 5,
            createdDate = currentDate,
            modifiedDate = currentDate,
            status = "active",
            questions = emptyMap()
        ),
        Survey(
            surveyId = "",
            creatorId = "user2",
            title = "Travel Preferences",
            content = "Where do you like to travel?",
            likes = 20,
            responses = 10,
            createdDate = currentDate,
            modifiedDate = currentDate,
            status = "active",
            questions = emptyMap()
        ),
        Survey(
            surveyId = "",
            creatorId = "user3",
            title = "Tech Gadgets",
            content = "What tech gadgets do you use?",
            likes = 30,
            responses = 15,
            createdDate = currentDate,
            modifiedDate = currentDate,
            status = "active",
            questions = emptyMap()
        )
    )

    addSurveysSequentially(repository, dummySurveys)
}

private fun addSurveysSequentially(repository: SurveyRepository, surveys: List<Survey>, index: Int = 0) {
    if (index < surveys.size) {
        repository.addSurvey(surveys[index]) {
            addSurveysSequentially(repository, surveys, index + 1)
        }
    }
}