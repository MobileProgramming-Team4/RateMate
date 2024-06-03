package com.example.ratemate.repository

import com.example.ratemate.data.Response
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ResponseRepository() {
    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("responses")

    // 응답 추가
    fun addResponse(response: Response) {
        val responseId = dbRef.push().key ?: return  // Generate a new key for each new response
        dbRef.child(responseId).setValue(response)
    }

    // 응답 삭제
    fun deleteResponse(responseId: String) {
        dbRef.child(responseId).removeValue()
    }

    // 응답 업데이트
    fun updateResponse(responseId: String, updatedFields: Map<String, Any>) {
        dbRef.child(responseId).updateChildren(updatedFields)
    }

    // 모든 응답 조회
    fun getAllResponses(): Flow<List<Response>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val responses = snapshot.children.mapNotNull { it.getValue(Response::class.java) }
                trySend(responses)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    // 특정 응답 조회
    fun getResponse(responseId: String): Flow<Response?> = callbackFlow {
        val listener = dbRef.child(responseId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val response = snapshot.getValue(Response::class.java)
                trySend(response)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { dbRef.child(responseId).removeEventListener(listener) }
    }
}