package com.example.ease

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.ease.model.AuthRepository
import com.example.ease.model.Model
import com.example.ease.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class addPostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        var view= inflater.inflate(R.layout.fragment_add_post, container, false)
        var profileName=view.findViewById<TextView>(R.id.profileName)
        var postText=view.findViewById<EditText>(R.id.addPostEditText)
        var postButton=view.findViewById<TextView>(R.id.postButton)
        var userServer= User.shared
        profileName.text = (activity as? MainActivity)?.getUserName()
        var email = (activity as? MainActivity)?.getUserEmail().toString()

        postButton.setOnClickListener {


            var postTextString=postText.text.toString()
            if(postTextString.isNotEmpty()){
                Model.shared.addPost(email,postTextString){ success, error ->
                    if (success) {
                        postText.text.clear()
                    } else {
                        // Handle the error
                    }
                }
                postText.text.clear()
            }
        }


        return view;
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addPostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addPostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}