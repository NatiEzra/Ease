package com.example.ease

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ease.model.AuthRepository
import com.example.ease.model.User
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [myProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class myProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var progressBar: ProgressBar
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
        // Inflate the layout for this fragment and check if the edit button is clicked
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)
        val editButton: TextView = view.findViewById(R.id.editButton)
        editButton.setOnClickListener {
            (activity as? MainActivity)?.editProfileButtonClicked()
        }

        val profileImage = view.findViewById<ImageView>(R.id.profileImage)
        progressBar = view.findViewById(R.id.profileImageProgressBar)
        val userServer = User.shared
        progressBar.visibility = View.VISIBLE
        profileImage.visibility=View.GONE
        userServer.getProfileImage { uri ->
            if (uri != null) {

                Picasso.get().load(uri).transform(CropCircleTransformation()).into(profileImage)
                progressBar.visibility = View.GONE
                profileImage.visibility=View.VISIBLE

            }
            else{
                progressBar.visibility = View.GONE
                profileImage.visibility=View.VISIBLE

            }
        }

        val logOutbtn=view.findViewById<Button>(R.id.logoutButton)
        val authServer=AuthRepository.shared
        logOutbtn.setOnClickListener {
            authServer.signOut()
            Toast.makeText(context,"You logged out, have a great day", Toast.LENGTH_LONG).show()
            (activity as? MainActivity)?.navigateToLogin()
        }
        val profileName = view.findViewById<TextView>(R.id.profileName)
        val profileEmail=view.findViewById<TextView>(R.id.profileEmail)
        profileName.text = (activity as? MainActivity)?.getUserName()
        profileEmail.text = (activity as? MainActivity)?.getUserEmail()


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment myProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            myProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}