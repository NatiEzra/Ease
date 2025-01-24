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
    fun getUser(onComplete: (Map<String, Any>?) -> Unit) {
        val userEmail = auth.currentUser?.email
        if (userEmail != null) {
            db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val userDocument = documents.documents[0]
                        val user= userDocument.data
                        onComplete(user)
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
    fun getUserByEmail(email: String, onComplete: (String) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val userDocument = documents.documents[0]
                    val username = userDocument.data?.get("name")
                    onComplete(username.toString())
                } else {
                    onComplete("user-not-found")

                }
            }
            .addOnFailureListener { e ->

            }
    }
}