package com.example.ratemate.repository

import com.example.ratemate.data.Response
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ResponseRepository {
    private val dbRef = FirebaseDatabase.getInstance().getReference("response")

    fun addResponse(response: Response) {
        val userId = response.userId.takeIf { it.isNotBlank() } ?: return
        dbRef.child(userId).setValue(response)
    }

    fun deleteResponse(userId: String) {
        dbRef.child(userId).removeValue()
    }

    fun updateResponse(userId: String, updatedFields: Map<String, Any>) {
        dbRef.child(userId).updateChildren(updatedFields)
    }

    fun getAllResponses(): Flow<List<Response>> = callbackFlow {
        val listener = dbRef.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val responses = snapshot.children.mapNotNull { it.getValue(Response::class.java) }
                trySend(responses).isSuccess
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { dbRef.removeEventListener(listener) }
    }

    fun getResponse(userId: String): Flow<Response?> = callbackFlow {
        val listener = dbRef.child(userId).addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val response = snapshot.getValue(Response::class.java)
                trySend(response).isSuccess
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { dbRef.child(userId).removeEventListener(listener) }
    }
}
