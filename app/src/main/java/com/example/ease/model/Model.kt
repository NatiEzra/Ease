package com.example.ease.model


data class Post(
    val postId: String,
    val profileName: String,
    var ProfileImage: String,
    var textPost: String,
    var imagePost: String,

    )

class Model  private constructor(){
    val posts: MutableList<Post> = ArrayList()

    companion object{
        val shared =Model()
    }
    init {
        var postsSize=posts.size
        for (i  in 0..5){
            val post=Post(  "postId$i","profileName$i", "ProfileImage$i", "textPost$i", "imagePost$i")
            posts.add(post)
        }
    }
}