package com.example.ratemate.repository

import com.example.ratemate.data.Comment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CommentRepository {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("comments")

    // 댓글 추가
    fun addComment(comment: Comment) {
        if (comment.commentId.isBlank()) {
            comment.commentId = dbRef.push().key ?: ""
        }
        dbRef.child(comment.commentId).setValue(comment)
    }

    // 댓글 삭제
    fun deleteComment(commentId: String) {
        dbRef.child(commentId).removeValue()
    }

    // 댓글 업데이트
    fun updateComment(commentId: String, updatedFields: Map<String, Any>) {
        dbRef.child(commentId).updateChildren(updatedFields)
    }

    // 모든 댓글 조회
    fun getAllComments(): Flow<List<Comment>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = snapshot.children.mapNotNull { it.getValue(Comment::class.java) }
                trySend(comments)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    // 특정 댓글 조회
    fun getComment(commentId: String): Flow<Comment?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comment = snapshot.getValue(Comment::class.java)
                trySend(comment)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.child(commentId).addValueEventListener(listener)
        awaitClose { dbRef.child(commentId).removeEventListener(listener) }
    }
}