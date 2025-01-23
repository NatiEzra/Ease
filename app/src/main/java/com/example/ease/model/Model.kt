package com.example.ease.model

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class Post(
    val postId: String,
    val profileName: String,
    var ProfileImage: String,
    var textPost: String,
    var imagePost: String,

    )

class Model  private constructor(){
    val db= Firebase.firestore
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    companion object{
        val shared =Model()
    }
    /*init {
        CoroutineScope(Dispatchers.Main).launch {
            posts=getPosts()
        }
*/
        /*
        var postsSize=posts.size
        for (i  in 0..5){
            val post=Post(  "postId$i","profileName$i", "ProfileImage$i", "textPost$i", "imagePost$i")
            posts.add(post)
        }*/


    suspend fun fetchPosts() {
        try {
            val documents = db.collection("posts").get().await()
            val fetchedPosts = documents.map { document ->
                Post(
                    postId = document.id,
                    profileName = document.getString("email") ?: "Unknown",
                    ProfileImage = "image",
                    textPost = document.getString("postText") ?: "No Content",
                    imagePost = "image"
                )
            }
            _posts.value = fetchedPosts // Update StateFlow
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching posts", e)
        }
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
    suspend fun getPosts(): MutableList<Post> {
        return try {
            val documents = db.collection("posts").get().await()
            val posts: MutableList<Post> = mutableListOf()
            for (document in documents) {
                val post = Post(
                    postId = document.id,
                    profileName = document.getString("email") ?: "name",
                    ProfileImage = "image",
                    textPost = document.getString("postText") ?: "post",
                    imagePost = "image"
                )
                posts.add(post)
            }
            Log.d("Firestore", "Successfully fetched ${posts.size} posts.")
            posts
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching posts", e)
            mutableListOf() // Return an empty list on failure
        }
    }
}
