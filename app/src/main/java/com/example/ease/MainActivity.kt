package com.example.ease

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ease.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private var profileName: String = ""
    private var userEmail: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //progressBar = findViewById(R.id.progressBar)
        //progressBar.findViewById<ProgressBar>(R.id.progressBar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // הגדרת הפרגמנט הראשוני

      /*  if (savedInstanceState == null) {
            replaceFragment(FeedFragment())
        }
        */

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, FeedFragment())
            commit()
            }

        val addPostButton = findViewById<ImageView>(R.id.add_icon)
        addPostButton.setOnClickListener {
            addPostButtonClicked(false, null) // Ensure `AddPostFragment` exists
            //addtobackstack

        }


        val homePageButton = findViewById<ImageView>(R.id.home_icon)
        homePageButton.setOnClickListener {
            homePageButtonClicked()
        }

        val profilePageButton=findViewById<ImageView>(R.id.profile_icon)
        profilePageButton.setOnClickListener{
            myProfilePageButtonClicked()
        }
        val myPostsbutton=findViewById<ImageView>(R.id.my_posts_icon)
        myPostsbutton.setOnClickListener{
            MyPostsButtonClicked()
        }


        var userServer= User.shared
        userServer.getUser { user ->
            if (user != null) {
                profileName = user["name"].toString()
                userEmail = user["email"].toString()
            }
        }
        val articlesButton=findViewById<ImageView>(R.id.articles_icon)
        articlesButton.setOnClickListener{
            articlesButtonClicked()
        }

    }

    fun MyPostsButtonClicked() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, MyPostsFragment())
            addToBackStack(null)
            commit()
        }
    }

    fun getUserName(): String {
        return profileName;
    }
    fun getUserEmail(): String {
        return userEmail;
    }
    fun refreshProfile(){
        var userServer= User.shared
        userServer.getUser { user ->
            if (user != null) {
                profileName = user["name"].toString()
                userEmail = user["email"].toString()
            }
        }
    }
    fun articlesButtonClicked(){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, articlesFragment())
            addToBackStack(null)
            commit()
        }
    }

// CHECK
    fun addPostButtonClicked(isEdit: Boolean, postId: String?) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, addPostFragment.newInstance(isEdit, postId))
            addToBackStack(null)
            commit()
        }
    }

    fun openInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    fun homePageButtonClicked() {

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // Replace the register fragment with the login fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, FeedFragment())
            commit()
        }


    }
    fun myProfilePageButtonClicked() {

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // Replace the register fragment with the login fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, myProfileFragment())
            commit()
        }


    }

    fun editProfileButtonClicked(){
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, editProfileFragment())
            commit()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    fun editPost(postId: String?) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, addPostFragment.newInstance(true, postId))
            addToBackStack(null)
            commit()
        }
    }
    fun navigateToLogin() {
        val intent = Intent(this, LoginRegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }


}
