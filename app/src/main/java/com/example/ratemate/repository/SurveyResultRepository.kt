package com.example.ratemate.repository

import com.example.ratemate.data.SurveyResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SurveyResultRepository() {

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("surveyResults")

    // 설문 결과 추가
    fun addSurveyResult(surveyResult: SurveyResult) {
        if (surveyResult.surveyId.isBlank()) {
            surveyResult.surveyId = dbRef.push().key ?: ""
        }
        dbRef.child(surveyResult.surveyId).setValue(surveyResult)
    }

    // 설문 결과 삭제
    fun deleteSurveyResult(surveyId: String) {
        dbRef.child(surveyId).removeValue()
    }

    // 설문 결과 업데이트
    fun updateSurveyResult(surveyId: String, updatedFields: Map<String, Any>) {
        dbRef.child(surveyId).updateChildren(updatedFields)
    }

    // 모든 설문 결과 조회
    fun getAllSurveyResults(): Flow<List<SurveyResult>> = callbackFlow {
        val listener = dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val surveyResults = snapshot.children.mapNotNull { it.getValue(SurveyResult::class.java) }
                trySend(surveyResults)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { dbRef.removeEventListener(listener) }
    }

    // 특정 설문 결과 조회
    fun getSurveyResult(surveyId: String): Flow<SurveyResult?> = callbackFlow {
        val listener = dbRef.child(surveyId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val surveyResult = snapshot.getValue(SurveyResult::class.java)
                trySend(surveyResult)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { dbRef.child(surveyId).removeEventListener(listener) }
    }
}