package com.example.ratemate.dummy

import com.example.ratemate.data.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

fun addDummyUserData() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val dbRef: DatabaseReference = database.getReference("user")

    val dummyUsers = listOf(
        User(
            userId = "user1",
            email = "user1@example.com",
            password = "password1",
            points = 100,
            createdDate = "2022-01-01",
            modifiedDate = "2022-01-01",
            status = "active",
            profileImage = "https://example.com/user1.jpg",
            surveysCreated = listOf("survey1", "survey2"),
            surveysParticipated = listOf("survey3"),
            PurchaseList = listOf()
        ),
        User(
            userId = "user2",
            email = "user2@example.com",
            password = "password2",
            points = 200,
            createdDate = "2022-02-01",
            modifiedDate = "2022-02-01",
            status = "active",
            profileImage = "https://example.com/user2.jpg",
            surveysCreated = listOf("survey3"),
            surveysParticipated = listOf("survey1", "survey2"),
            PurchaseList = listOf()
        ),
        // 추가적인 더미 사용자 데이터
    )

    for (user in dummyUsers) {
        val userId = user.userId.takeIf { it.isNotBlank() } ?: dbRef.push().key ?: continue
        dbRef.child(userId).setValue(user)
    }
}

