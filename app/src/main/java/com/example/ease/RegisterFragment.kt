package com.example.ease

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.ease.model.AuthRepository
import com.example.ease.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var emailField: TextView
    private lateinit var passwordField: TextView
    private lateinit var confirmPasswordField: TextView
    private lateinit var registerButton: Button
    private lateinit var name: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val backButton: Button = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            // Call the activity's method to replace the fragment
            (activity as? LoginRegisterActivity)?.onBackButtonClicked(it)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailField = view.findViewById(R.id.email_field)
        passwordField = view.findViewById(R.id.password_field)
        confirmPasswordField = view.findViewById(R.id.confirm_password_field)
        registerButton = view.findViewById(R.id.Register_button)
        name = view.findViewById(R.id.username_field)

        setupListeners()

    }

    private fun setupListeners() {
        var authServer=AuthRepository.shared
        var userServer=User.shared
        registerButton.setOnClickListener {

            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()
            val username = name.text.toString()
            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            authServer.registerUser(email, password) { success, error ->
                if (success) {
                    userServer.createUser(username,email){success,error->
                        if (success) {
                            Log.d("TAG", "User added to database")
                        } else {
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    }
                    (activity as? LoginRegisterActivity)?.navigateToHome()
                    Log.d("TAG", "User registered successfully ")
                } else {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}