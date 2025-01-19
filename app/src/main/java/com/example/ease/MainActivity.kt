package com.example.ease

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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
            addPostButtonClicked() // Ensure `AddPostFragment` exists
            //addtobackstack

        }


        val homePageButton = findViewById<ImageView>(R.id.home_icon)
        homePageButton.setOnClickListener {
            homePageButtonClicked()
        }

    }


    fun addPostButtonClicked() {
        // Replace the register fragment with the login fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, addPostFragment())
            addToBackStack(null)
            commit()
        }
    }

    fun homePageButtonClicked() {

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // Replace the register fragment with the login fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, FeedFragment())
            commit()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
