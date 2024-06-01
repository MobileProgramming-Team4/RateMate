package com.example.ratemate.repository

import com.example.ratemate.data.StoreItem
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class StoreItemRepository() {

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Comments")

    // 상점 아이템 추가
    fun addItem(storeItem: StoreItem) {
        val itemId = storeItem.itemId.takeIf { it.isNotBlank() } ?: dbRef.push().key ?: return
        storeItem.itemId = itemId
        dbRef.child(itemId).setValue(storeItem)
    }

    // 상점 아이템 삭제
    fun deleteItem(itemId: String) {
        dbRef.child(itemId).removeValue()
    }

    // 상점 아이템 업데이트
    fun updateItem(itemId: String, updatedFields: Map<String, Any>) {
        dbRef.child(itemId).updateChildren(updatedFields)
    }

    // 모든 상점 아이템 조회
    fun getAllItems(): Flow<List<StoreItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { it.getValue(StoreItem::class.java) }
                trySend(items)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        dbRef.addValueEventListener(listener)
        awaitClose { dbRef.removeEventListener(listener) }
    }

    // 특정 상점 아이템 조회
    fun getItem(itemId: String): Flow<StoreItem?> = callbackFlow {
        val listener = dbRef.child(itemId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(StoreItem::class.java)
                trySend(item)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { dbRef.child(itemId).removeEventListener(listener) }
    }
}