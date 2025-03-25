package com.example.ease

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ease.model.User
import com.example.ease.model.local.AppDatabase
import com.example.ease.model.local.UserEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var progressBar: ProgressBar
    private var profileName: String = ""
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                    val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//
//                    if (currentFragment !is FeedFragment) {
//                        homePageButtonClicked()
//                    } else {
//                        finishAffinity()
//                    }
//            }
//        })

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
            ?: throw IllegalStateException("NavController not found")

        // Set up bottom navigation (if needed)
        findViewById<ImageView>(R.id.home_icon).setOnClickListener {
            navController.navigate(R.id.feedFragment)
        }

        findViewById<ImageView>(R.id.articles_icon).setOnClickListener {
            navController.navigate(R.id.articlesFragment)
        }

        findViewById<ImageView>(R.id.my_posts_icon).setOnClickListener {
            navController.navigate(R.id.myPostsFragment)
        }

        findViewById<ImageView>(R.id.add_icon).setOnClickListener {
            val currentDestination = navController.currentDestination?.id
            val action = when (currentDestination) {
                R.id.feedFragment -> FeedFragmentDirections.actionFeedFragmentToAddPostFragment(isEdit=false, postId = null)
                R.id.myPostsFragment -> MyPostsFragmentDirections.actionMyPostsFragmentToAddPostFragment(isEdit=false, postId = null)
                R.id.myProfileFragment -> myProfileFragmentDirections.actionMyProfileFragmentToAddPostFragment(isEdit=false, postId = null)
                R.id.articlesFragment -> articlesFragmentDirections.actionArticlesFragmentToAddPostFragment(isEdit=false, postId = null)
                R.id.editProfileFragment -> editProfileFragmentDirections.actionEditProfileFragmentToAddPostFragment(isEdit=false, postId = null)
                else -> return@setOnClickListener
            }
            navController.navigate(action)
        }

        findViewById<ImageView>(R.id.profile_icon).setOnClickListener {
            navController.navigate(R.id.myProfileFragment)
        }


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

        //navController.navigate(R.id.feedFragment)

//        val addPostButton = findViewById<ImageView>(R.id.add_icon)
//        addPostButton.setOnClickListener {
//            addPostButtonClicked(false, null) // Ensure `AddPostFragment` exists
//            //addtobackstack
//
//        }
//
//
//        val homePageButton = findViewById<ImageView>(R.id.home_icon)
//        homePageButton.setOnClickListener {
//            homePageButtonClicked()
//        }
//
//        val profilePageButton=findViewById<ImageView>(R.id.profile_icon)
//        profilePageButton.setOnClickListener{
//            myProfilePageButtonClicked()
//        }
//        val myPostsbutton=findViewById<ImageView>(R.id.my_posts_icon)
//        myPostsbutton.setOnClickListener{
//            MyPostsButtonClicked()
//        }


        var userServer= User.shared
        userServer.getUser { user ->
            if (user != null) {
                val userDao = AppDatabase.getInstance(applicationContext).userDao()
                lifecycleScope.launch {
                    userDao.clear() // optional
                    userDao.insert(UserEntity(email = user["email"].toString(), name = user["name"].toString(), profileImageUrl = user["image"] as? String))
                }
            }
        }
//        val articlesButton=findViewById<ImageView>(R.id.articles_icon)
//        articlesButton.setOnClickListener{
//            articlesButtonClicked()
//        }

    }

    fun MyPostsButtonClicked() {
        navController.navigate(R.id.myPostsFragment)
    }
    suspend fun getCachedUser(): UserEntity? {
        return AppDatabase.getInstance(applicationContext).userDao().getCurrentUser()
    }
    fun getUserName(callback: (String?) -> Unit) {
        lifecycleScope.launch {
            val user = getCachedUser()
            callback(user?.name)
        }
    }

    fun getUserEmail(callback: (String?) -> Unit) {
        lifecycleScope.launch {
            val user = getCachedUser()
            callback(user?.email)
        }
    }
//    fun getUserName(): String {
//        return profileName;
//    }
//    fun getUserEmail(): String {
//        return userEmail;
//    }
fun refreshProfile() {
    val userServer = User.shared
    userServer.getUser { user ->
        if (user != null) {
            val name = user["name"].toString()
            val email = user["email"].toString()
            val image= user["image"] as? String

            // Save to Room
            lifecycleScope.launch {
                val userDao = AppDatabase.getInstance(applicationContext).userDao()
                userDao.clear() // Optional: clear old user
                userDao.insert(UserEntity(email = email, name = name, profileImageUrl = image))
            }
        }
    }
}
    fun articlesButtonClicked(){
        navController.navigate(R.id.articlesFragment)
    }

// CHECK


    fun openInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    fun homePageButtonClicked() {

        navController.navigate(R.id.feedFragment)

    }
    fun myProfilePageButtonClicked() {

        navController.navigate(R.id.myProfileFragment)



    }

    fun editProfileButtonClicked(){
        navController.navigate(R.id.editProfileFragment)

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
        val action = MyPostsFragmentDirections.actionMyPostsFragmentToAddPostFragment(isEdit = true, postId = postId)
        navController.navigate(action)
    }
    fun navigateToLogin() {
        val intent = Intent(this, LoginRegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }





}
