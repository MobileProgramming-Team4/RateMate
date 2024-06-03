package com.example.ratemate.repository

import com.example.ratemate.data.Like
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LikeRepository() {

    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("likes")

    // 좋아요 항목 생성 또는 업데이트
    fun setLike(likeId: String, like: Like) {
        dbRef.child(likeId).setValue(like)
    }

    // 좋아요 항목 삭제
    fun deleteLike(likeId: String) {
        dbRef.child(likeId).removeValue()
    }

    // 모든 좋아요 항목 조회
    fun getAllLikes(): Flow<List<Like>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likes = snapshot.children.mapNotNull { it.getValue(Like::class.java) }
                trySend(likes)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    // 특정 좋아요 항목 조회
    fun getLike(likeId: String): Flow<Like?> = callbackFlow {
        val listener = dbRef.child(likeId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val like = snapshot.getValue(Like::class.java)
                trySend(like)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { dbRef.child(likeId).removeEventListener(listener) }
    }
}