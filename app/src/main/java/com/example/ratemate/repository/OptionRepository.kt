package com.example.ratemate.repository

import android.util.Log
import com.example.ratemate.data.Option
import com.example.ratemate.data.Survey
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class OptionRepository {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("surveys")

    // 선택지 추가
    fun addOptionToQuestion(surveyId: String, questionId: String, option: Option, callback: (() -> Unit)? = null) {
        dbRef.child(surveyId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                if (survey != null) {
                    val question = survey.questions.find { it.questionId == questionId }
                    if (question != null) {
                        val optionId = dbRef.child(surveyId).child("questions").child(questionId).child("options").push().key ?: return
                        option.optionId = optionId
                        question.options.add(option)
                        dbRef.child(surveyId).setValue(survey).addOnCompleteListener {
                            callback?.invoke()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OptionRepository", "Failed to add option: ${error.message}")
            }
        })
    }

    // 선택지 삭제
    fun deleteOptionFromQuestion(surveyId: String, questionId: String, optionId: String) {
        dbRef.child(surveyId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                if (survey != null) {
                    val question = survey.questions.find { it.questionId == questionId }
                    if (question != null) {
                        question.options.removeAll { it.optionId == optionId }
                        dbRef.child(surveyId).setValue(survey)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OptionRepository", "Failed to delete option: ${error.message}")
            }
        })
    }

    // 선택지 업데이트
    fun updateOptionInQuestion(surveyId: String, questionId: String, option: Option) {
        dbRef.child(surveyId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                if (survey != null) {
                    val question = survey.questions.find { it.questionId == questionId }
                    if (question != null) {
                        val index = question.options.indexOfFirst { it.optionId == option.optionId }
                        if (index != -1) {
                            question.options[index] = option
                            dbRef.child(surveyId).setValue(survey)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OptionRepository", "Failed to update option: ${error.message}")
            }
        })
    }

    // 질문에 포함된 모든 선택지 조회
    fun getOptionsForQuestion(surveyId: String, questionId: String): Flow<List<Option>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                val question = survey?.questions?.find { it.questionId == questionId }
                val options = question?.options ?: mutableListOf()
                trySend(options).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OptionRepository", "Failed to fetch options: ${error.message}")
                close(error.toException())
            }
        }
        dbRef.child(surveyId).child("questions").child(questionId).child("options").addValueEventListener(listener)
        awaitClose {
            dbRef.child(surveyId).child("questions").child(questionId).child("options").removeEventListener(listener)
        }
    }

    // 특정 선택지 조회
    fun getOption(surveyId: String, questionId: String, optionId: String): Flow<Option?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val survey = snapshot.getValue(Survey::class.java)
                val question = survey?.questions?.find { it.questionId == questionId }
                val option = question?.options?.find { it.optionId == optionId }
                trySend(option).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OptionRepository", "Failed to fetch option: ${error.message}")
                close(error.toException())
            }
        }
        dbRef.child(surveyId).child("questions").child(questionId).child("options").child(optionId).addValueEventListener(listener)
        awaitClose {
            dbRef.child(surveyId).child("questions").child(questionId).child("options").child(optionId).removeEventListener(listener)
        }
    }
}
