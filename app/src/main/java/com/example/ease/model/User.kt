package com.example.ease.model

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User{
    var auth=AuthRepository.shared
    companion object{
        val shared =User()
    }

    val db = Firebase.firestore

    fun createUser(name: String, email: String, onComplete: (Boolean, String?) -> Unit) {
        val user = hashMapOf(
            "name" to name,
            "email" to email
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.localizedMessage)
            }
    }
    fun getName(onComplete: (String?) -> Unit) {
        val userEmail = auth.currentUser?.email
        if (userEmail != null) {
            db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val userDocument = documents.documents[0]
                        val userName = userDocument.getString("name")
                        onComplete(userName)
                    } else {
                        onComplete(null)
                    }
                }
                .addOnFailureListener { e ->
                    onComplete(null)
                }
        } else {
            onComplete(null)
        }
    }
}