package com.example.ratemate.repository

import com.example.ratemate.data.QnA
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class QnARepository {

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("qna")

    fun addQnA(qna: QnA) {
        dbRef.child(qna.order.toString()).setValue(qna)
    }

    fun deleteQnA(order: Int) {
        dbRef.child(order.toString()).removeValue()
    }

    fun updateQnA(order: Int, updatedFields: Map<String, Any>) {
        dbRef.child(order.toString()).updateChildren(updatedFields)
    }

    fun getAllQnAs(): Flow<List<QnA>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val qnas = snapshot.children.mapNotNull { it.getValue(QnA::class.java) }
                trySend(qnas)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    fun getQnA(order: Int): Flow<QnA?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val qna = snapshot.getValue(QnA::class.java)
                trySend(qna)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.child(order.toString()).addListenerForSingleValueEvent(listener)
        awaitClose { dbRef.child(order.toString()).removeEventListener(listener) }
    }

}