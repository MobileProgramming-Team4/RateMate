package com.example.ratemate.repository

import com.example.ratemate.data.Option
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class OptionRepository {

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Options")

    // 옵션 생성 또는 업데이트
    fun setOption(optionId: String, option: Option) {
        dbRef.child(optionId).setValue(option)
    }

    // 옵션 삭제
    fun deleteOption(optionId: String) {
        dbRef.child(optionId).removeValue()
    }

    // 모든 옵션 조회
    fun getAllOptions(): Flow<List<Option>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val options = snapshot.children.mapNotNull { it.getValue(Option::class.java) }
                trySend(options)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    // 특정 옵션 조회
    fun getOption(optionId: String): Flow<Option?> = callbackFlow {
        val listener = dbRef.child(optionId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val option = snapshot.getValue(Option::class.java)
                trySend(option)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { dbRef.child(optionId).removeEventListener(listener) }
    }
}
