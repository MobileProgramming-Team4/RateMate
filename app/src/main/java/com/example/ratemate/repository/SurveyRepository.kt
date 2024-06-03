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

class SurveyRepository {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("surveys")

    // 설문 추가
    fun addSurvey(survey: Survey) {
        dbRef.child(survey.surveyId).setValue(survey)
    }

    // 설문 삭제
    fun deleteSurvey(surveyId: String) {
        dbRef.child(surveyId).removeValue()
    }

    // 설문 업데이트
    fun updateSurvey(surveyId: String, updatedFields: Map<String, Any>) {
        dbRef.child(surveyId).updateChildren(updatedFields)
    }

    // 모든 설문 조회
    fun getAllSurveys(): Flow<List<Survey>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val surveys = snapshot.children.mapNotNull { it.getValue(Survey::class.java) }
                trySend(surveys).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                // 여기서 에러 처리를 할 수 있습니다. 예를 들어, 로그를 남기거나 특정 콜백을 호출할 수 있습니다.
                Log.e("SurveyDao", "Failed to fetch surveys: ${error.message}")
                close(error.toException()) // Flow를 적절하게 닫고, 예외를 포함시킬 수 있습니다.
            }
        }

        dbRef.addValueEventListener(listener)
        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }

    // 특정 설문 조회
    fun getSurvey(surveyId: String): Flow<Survey?> = callbackFlow {
        val listener = dbRef.child(surveyId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                trySend(survey).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SurveyDao", "Failed to fetch survey: ${error.message}")
                close(error.toException()) // 에러 발생 시 Flow를 닫습니다.
            }
        })

        awaitClose {
            dbRef.child(surveyId).removeEventListener(listener)
        }
    }
}
