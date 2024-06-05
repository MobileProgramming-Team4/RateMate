package com.example.ratemate.repository

import android.util.Log
import com.example.ratemate.data.SurveyV2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SurveyV2Repository {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("surveysV2")

    fun addSurvey(survey : SurveyV2){
        val surveyId = survey.surveyId.takeIf { it.isNotBlank() } ?: dbRef.push().key ?: return
        survey.surveyId = surveyId
        Log.d("설문 추가", "addSurvey: $survey")
        dbRef.child(surveyId).setValue(survey)
    }


    fun deleteSurvey(surveyId : String){
        dbRef.child(surveyId).removeValue()
    }

    fun updateSurvey(surveyId : String, updatedFields : Map<String, Any>){
        dbRef.child(surveyId).updateChildren(updatedFields)
    }
    fun getAllSurveys(): Flow<List<SurveyV2>> = callbackFlow{
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val surveys = snapshot.children.mapNotNull { it.getValue(SurveyV2::class.java) }
                trySend(surveys)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    fun getSurvey(surveyId : String): Flow<SurveyV2?> = callbackFlow{
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(SurveyV2::class.java)
                trySend(survey).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        dbRef.child(surveyId).addListenerForSingleValueEvent(listener)
        awaitClose { dbRef.child(surveyId).removeEventListener(listener) }

    }

}
