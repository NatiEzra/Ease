package com.example.ease.model

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


data class Post(
    val postId: String,
    val profileName: String,
    var ProfileImage: String,
    var textPost: String,
    var imagePost: String,

    )

class Model  private constructor(){
    val db= Firebase.firestore
    @Volatile
    var posts: MutableList<Post> = mutableListOf()

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
    fun addPost(email: String,postText: String, onComplete: (Boolean, String?) -> Unit) {
        val post = hashMapOf(
            "email" to email,
            "postText" to postText
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
    fun getPosts(onComplete: (MutableList<Post>) -> Unit) {
        db.collection("posts")
            .get()
            .addOnSuccessListener { documents ->
                val posts : MutableList<Post> = mutableListOf()
                for (document in documents) {
                    val post = Post(
                        postId=document.id,
                        profileName = document.getString("email") ?: "name",
                        ProfileImage = "image",
                        textPost = document.getString("postText") ?: "post",
                        imagePost = "image"
                    )
                    posts.add(post)
                }
                Log.d("Firestore", "Successfully fetched ${posts.size} posts.")
                onComplete(posts)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching posts", e)
                onComplete(mutableListOf()) // Return an empty list on failure
                }
            }
}
