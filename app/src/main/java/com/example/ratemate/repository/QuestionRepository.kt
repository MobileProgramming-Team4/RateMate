package com.example.ratemate.repository

import android.util.Log
import com.example.ratemate.data.Question
import com.example.ratemate.data.Survey
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class QuestionRepository {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("surveys")

    // 설문에 질문 추가
    fun addQuestionToSurvey(surveyId: String, question: Question, callback: (() -> Unit)? = null) {
        dbRef.child(surveyId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                if (survey != null) {
                    val questionId = dbRef.child(surveyId).child("questions").push().key ?: return
                    question.questionId = questionId
                    survey.questions.add(question)
                    dbRef.child(surveyId).setValue(survey).addOnCompleteListener {
                        callback?.invoke()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("QuestionRepository", "Failed to add question: ${error.message}")
            }
        })
    }

    // 설문에서 질문 삭제
    fun deleteQuestionFromSurvey(surveyId: String, questionId: String) {
        dbRef.child(surveyId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                if (survey != null) {
                    survey.questions.removeAll { it.questionId == questionId }
                    dbRef.child(surveyId).setValue(survey)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("QuestionRepository", "Failed to delete question: ${error.message}")
            }
        })
    }

    // 설문에서 질문 업데이트
    fun updateQuestionInSurvey(surveyId: String, question: Question) {
        dbRef.child(surveyId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                if (survey != null) {
                    val index = survey.questions.indexOfFirst { it.questionId == question.questionId }
                    if (index != -1) {
                        survey.questions[index] = question
                        dbRef.child(surveyId).setValue(survey)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("QuestionRepository", "Failed to update question: ${error.message}")
            }
        })
    }

    // 설문에 포함된 모든 질문 조회
    fun getQuestionsForSurvey(surveyId: String): Flow<List<Question>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                val questions = survey?.questions ?: mutableListOf()
                trySend(questions).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("QuestionRepository", "Failed to fetch questions: ${error.message}")
                close(error.toException())
            }
        }
        dbRef.child(surveyId).addValueEventListener(listener)
        awaitClose {
            dbRef.child(surveyId).removeEventListener(listener)
        }
    }

    // 특정 질문 조회
    fun getQuestion(surveyId: String, questionId: String): Flow<Question?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                val question = survey?.questions?.find { it.questionId == questionId }
                trySend(question).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("QuestionRepository", "Failed to fetch question: ${error.message}")
                close(error.toException())
            }
        }
        dbRef.child(surveyId).addValueEventListener(listener)
        awaitClose {
            dbRef.child(surveyId).removeEventListener(listener)
        }
    }
}
