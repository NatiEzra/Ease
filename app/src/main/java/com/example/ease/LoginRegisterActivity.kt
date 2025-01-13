package com.example.ease
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class LoginRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_login_register)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
                 supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, loginFragment())
                commit()
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error in onCreate: ${e.message}")
        }
    }


    fun onNewMemberClicked(view: View) {
        Log.d("TAG", "This is a debug message")
        // Replace the login fragment with the register fragment

             supportFragmentManager.beginTransaction().apply {
                 replace(R.id.fragment_container, RegisterFragment())
                 addToBackStack(null)  // Optional: adds this transaction to the back stack
                 commit()
             }


    }
}
