package com.example.ease

import android.app.AlertDialog
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.collection.emptyLongSet
import com.example.ease.model.Model
import com.example.ease.model.User
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


private var cameraLauncher: ActivityResultLauncher<Void?>? = null
private var galleryLauncher: ActivityResultLauncher<String>? = null


/**
 * A simple [Fragment] subclass.
 * Use the [editProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class editProfileFragment : Fragment() {
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
        var view= inflater.inflate(R.layout.fragment_edit_profile, container, false)
        var editIcon = view.findViewById<ImageView>(R.id.editIcon)
        var profileImage = view.findViewById<ImageView>(R.id.profileImage)
        var addedImageToProfile: Boolean = false
        var saveButton = view.findViewById<Button>(R.id.saveButton)
        var userServer= User.shared
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            //binding?.imageView?.setImageBitmap(bitmap)
            if(bitmap!=null){
                profileImage.setImageBitmap(bitmap)
                addedImageToProfile = true
            }

        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                profileImage.setImageURI(uri)
                addedImageToProfile = true
            }
        }

        editIcon.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            val customTitle = layoutInflater.inflate(R.layout.dialog_title, null)
            val builder = AlertDialog.Builder(requireContext())
            builder.setCustomTitle(customTitle)
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> cameraLauncher?.launch(null)
                    1 -> galleryLauncher?.launch("image/*")
                }
            }
            builder.show()
        }

        saveButton.setOnClickListener {
            if (addedImageToProfile) {
                profileImage.isDrawingCacheEnabled = true
                profileImage.buildDrawingCache()
                val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
                userServer.editUser(bitmap) { success, error ->
                    if (success) {
                        Toast.makeText(context, "Profile image updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                }


            }

        }

        userServer.getProfileImage { uri ->
            if (uri != null) {
                Picasso.get().load(uri).into(profileImage)
            }
        }




        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment editProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            editProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}