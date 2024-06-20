package com.example.ratemate.repository

import android.util.Log
import com.example.ratemate.data.Survey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SurveyRepository {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("surveys")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // 설문 추가
    fun addSurvey(survey: Survey, callback: (() -> Unit)? = null) {
        dbRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val maxId = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.maxOrNull() ?: 0
                val newSurveyId = (maxId + 1).toString()
                survey.surveyId = newSurveyId
                val currentDate = dateFormat.format(Date())
                survey.createdDate = currentDate
                survey.modifiedDate = currentDate
                dbRef.child(newSurveyId).setValue(survey).addOnCompleteListener {
                    callback?.invoke()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SurveyRepository", "Failed to fetch last survey id: ${error.message}")
            }
        })
    }

    // 설문 삭제
    fun deleteSurvey(surveyId: String) {
        dbRef.child(surveyId).removeValue()
    }

    // 설문 업데이트 (질문 목록 유지)
    fun updateSurvey(surveyId: String, updatedFields: Map<String, Any>) {
        dbRef.child(surveyId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                if (survey != null) {
                    val updatedSurvey = survey.copy(
                        creatorId = updatedFields["creatorId"] as? String ?: survey.creatorId,
                        title = updatedFields["title"] as? String ?: survey.title,
                        content = updatedFields["content"] as? String ?: survey.content,
                        likes = updatedFields["likes"] as? Int ?: survey.likes,
                        responses = updatedFields["responses"] as? Int ?: survey.responses,
                        modifiedDate = dateFormat.format(Date()),
                        status = updatedFields["status"] as? String ?: survey.status
                    )
                    dbRef.child(surveyId).setValue(updatedSurvey)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SurveyRepository", "Failed to update survey: ${error.message}")
            }
        })
    }

    // 모든 설문 조회
    fun getAllSurveys(): Flow<List<Survey>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val surveys = snapshot.children.mapNotNull { it.getValue(Survey::class.java) }
                trySend(surveys).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SurveyDao", "Failed to fetch surveys: ${error.message}")
                close(error.toException())
            }
        }

        dbRef.addValueEventListener(listener)
        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }

    // 특정 설문 조회
    fun getSurvey(surveyId: String): Flow<Survey?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                trySend(survey).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SurveyDao", "Failed to fetch survey: ${error.message}")
                close(error.toException())
            }
        }

        dbRef.child(surveyId).addValueEventListener(listener)
        awaitClose {
            dbRef.child(surveyId).removeEventListener(listener)
        }
    }
}
