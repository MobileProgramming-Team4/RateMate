package com.example.ratemate.repository

import com.example.ratemate.data.PointTransaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PointTransactionRepository() {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("PointTransactions")

    // 포인트 거래 추가
    fun addPointTransaction(transaction: PointTransaction) {
        val key = transaction.transactionId.takeIf { it.isNotBlank() } ?: dbRef.push().key ?: return
        transaction.transactionId = key
        dbRef.child(key).setValue(transaction)
    }

    // 포인트 거래 삭제
    fun deletePointTransaction(transactionId: String) {
        dbRef.child(transactionId).removeValue()
    }

    // 포인트 거래 업데이트
    fun updatePointTransaction(transactionId: String, updatedFields: Map<String, Any>) {
        dbRef.child(transactionId).updateChildren(updatedFields)
    }

    // 모든 포인트 거래 조회
    fun getAllPointTransactions(): Flow<List<PointTransaction>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = snapshot.children.mapNotNull { it.getValue(PointTransaction::class.java) }
                trySend(transactions)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    // 특정 포인트 거래 조회
    fun getPointTransaction(transactionId: String): Flow<PointTransaction?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transaction = snapshot.getValue(PointTransaction::class.java)
                trySend(transaction)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.child(transactionId).addValueEventListener(listener)
        awaitClose { dbRef.child(transactionId).removeEventListener(listener) }
    }
}