package com.example.ratemate.repository

import com.example.ratemate.data.Survey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRepository {
    private val dbRef = FirebaseDatabase.getInstance().getReference("surveys")

    fun getSurveys(onResult: (List<Survey>) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val surveys = snapshot.children.mapNotNull { it.getValue(Survey::class.java) }
                onResult(surveys)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database error: $error")
            }
        })
    }
}