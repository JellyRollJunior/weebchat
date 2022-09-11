package com.example.weebchat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weebchat.databinding.FragmentSignupBinding
import com.example.weebchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.math.log

class SignupFragment : Fragment() {

    private val logTAG = "Signup Fragment"
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupFragment = this
    }

    fun getPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT) .setType("image/*")
        // launch gallery intent if there exists an gallery app
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            Log.d(logTAG, "Upload photo implicit intent launched")
            startForResult.launch("image/*")
        }
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.d(logTAG, "Selected photo Uri is set")
            selectedPhotoUri = uri
        }
    }

    fun signup() {
        val name = binding.nameInputEditText.text.toString()
        val email = binding.emailInputEditText.text.toString()
        val password = binding.passwordInputEditText.text.toString()
        val confirmPassword = binding.confirmPasswordInputEditText.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter a name, email, and password", Toast.LENGTH_SHORT).show()
            return
        } else if (password == confirmPassword) {
            // sign up
            setErrorTextField(false)
            // use requireActivity() instead of this since we are in a fragment, not an activity
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireActivity(), "Sign up successful", Toast.LENGTH_SHORT).show()
                        uploadImageToFirebaseStorage()
                    } else {
                        Toast.makeText(requireActivity(), "Error signing up occurred", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // return UI error
            setErrorTextField(true)
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) {
            Log.d(logTAG, "Selected photo Uri is null")
            return
        }
        // creates a random string name for the file
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(logTAG, "Successfully uploaded image: $filename")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(logTAG, "File Location: $it")
                        saveUserToFirebaseStorage(it.toString())
                    }
                } else {
                    Log.d(logTAG, "Upload image unsuccessful: $filename")
                }
            }

    }

    private fun saveUserToFirebaseStorage(profileImageUrl: String) {
        val uid = auth.uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(binding.nameInputEditText.text.toString(), uid, profileImageUrl)

        ref.setValue(user)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(logTAG, "User successfully saved to database")
                } else {
                    Log.d(logTAG, "User could not be saved to database")
                }
            }
    }

    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.passwordTextField.isErrorEnabled = true
            binding.passwordTextField.error = getString(R.string.passwords_do_not_match)
            binding.confirmPasswordTextField.isErrorEnabled = true
        } else {
            binding.passwordTextField.isErrorEnabled = false
            binding.confirmPasswordTextField.isErrorEnabled = false
        }
    }
}



