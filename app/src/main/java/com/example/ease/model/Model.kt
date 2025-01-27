package com.example.ease.model

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale


data class Post(
    val postId: String,
    val profileName: String,
    var ProfileImage: String,
    var textPost: String,
    var imagePost: String,
    var date: java.util.Date,

    )

class Model  private constructor(){
    val db= Firebase.firestore
    @Volatile
    var posts: MutableList<Post> = mutableListOf()
    var userServer= User.shared
    val cloudinaryModel= CloudinaryModel()

    companion object{
        val shared =Model()
    }
    init {
        getPosts { retrievedPosts ->
            posts = retrievedPosts
        }
        /*
        var postsSize=posts.size
        for (i  in 0..5){
            val post=Post(  "postId$i","profileName$i", "ProfileImage$i", "textPost$i", "imagePost$i")
            posts.add(post)
        }*/


    }
    fun addPost(email: String, image: Bitmap?, postText: String, onComplete: (Boolean, String?) -> Unit) {
        val post = hashMapOf(
            "email" to email,
            "postText" to postText,
            "date" to System.currentTimeMillis()
        )

        db.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.localizedMessage)
            }
    }
    fun uploadImageToCloudinary(bitmap: Bitmap, name : String, onSuccess: (String?) -> Unit, onError: (String?) -> Unit) {
        cloudinaryModel.uploadImage(bitmap, name, onSuccess, onError)
    }
    fun getPosts(onComplete: (MutableList<Post>) -> Unit) {

        var username : String ="";


        db.collection("posts")
            .get()
            .addOnSuccessListener { documents ->
                val posts : MutableList<Post> = mutableListOf()
                var pendingCallbacks = documents.size() // Number of documents to process

                if (pendingCallbacks == 0) {
                    // If there are no documents, complete immediately
                    onComplete(posts)
                    return@addOnSuccessListener
                }
                for (document in documents) {
                    val email = document.getString("email") ?: "email"
                    userServer.getUserByEmail(email) { username ->
                        val post = Post(
                            postId = document.id,
                            profileName = username,
                            ProfileImage = "image",
                            textPost = document.getString("postText") ?: "post",
                            imagePost = "image",
                            date = java.util.Date(document.getLong("date") ?: 0)
                        )
                        posts.add(post)
                        pendingCallbacks--

                        // When all callbacks are complete, sort and return the list
                        if (pendingCallbacks == 0) {
                            posts.sortByDescending { it.date }
                            Log.d("Firestore", "Successfully fetched ${posts.size} posts.")
                            onComplete(posts)
                        }
                    }



                    /*val post = Post(
                        postId=document.id,
                        profileName = username,
                        ProfileImage = "image",
                        textPost = document.getString("postText") ?: "post",
                        imagePost = "image",
                        date=java.util.Date(document.getLong("date") ?: 0)
                        //date = document.getData()["date"] as java.util.Date
                    )
                    posts.add(post)

                     */
                }
                posts.sortByDescending { it.date }
                Log.d("Firestore", "Successfully fetched ${posts.size} posts.")
                onComplete(posts)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching posts", e)
                onComplete(mutableListOf()) // Return an empty list on failure
            }
    }
}
