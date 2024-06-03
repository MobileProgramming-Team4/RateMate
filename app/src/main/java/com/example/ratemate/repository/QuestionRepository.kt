package com.example.ratemate.repository

import com.example.ratemate.data.Question
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class QuestionRepository() {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("Questions")

    // 질문 추가
    fun insertQuestion(question: Question) {
        val key = if (question.questionId.isBlank()) dbRef.push().key else question.questionId
        key?.let {
            dbRef.child(it).setValue(question)
        }
    }

    // 질문 삭제
    fun deleteQuestion(questionId: String) {
        dbRef.child(questionId).removeValue()
    }

    // 질문 업데이트
    fun updateQuestion(questionId: String, updatedFields: Map<String, Any>) {
        dbRef.child(questionId).updateChildren(updatedFields)
    }

    // 모든 질문 조회
    fun getAllQuestions(): Flow<List<Question>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = snapshot.children.mapNotNull { it.getValue(Question::class.java) }
                trySend(questions)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        dbRef.addValueEventListener(listener)
        awaitClose {
            dbRef.removeEventListener(listener)
        }
    }

    // 특정 내용을 가진 질문 조회
    fun getQuestionsByContent(content: String): Flow<List<Question>> = callbackFlow {
        val query = dbRef.orderByChild("content").equalTo(content)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = snapshot.children.mapNotNull { it.getValue(Question::class.java) }
                trySend(questions)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        query.addValueEventListener(listener)
        awaitClose {
            query.removeEventListener(listener)
        }
    }
}