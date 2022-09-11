package com.example.weebchat

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.weebchat.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class SignupFragment : Fragment() {

    private val logTAG = "Signup Fragment"
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth

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

    fun uploadPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT) .setType("image/*")
        // launch gallery intent if there exists an gallery app
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            Log.d(logTAG, "Upload photo implicit intent launched")
            startForResult.launch("image/*")
        }
    }

    var selectedPhotoUri: Uri? = null

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
                Log.d(logTAG, "idk whats going on")
                selectedPhotoUri = uri
            }

    }

    private fun uploadImageToFirebaseStorage() {
        Log.d(logTAG, "at least im here")
        if (selectedPhotoUri == null) {
            Log.d(logTAG, "its null")
            return
        }
        Log.d(logTAG, "at least im here again")
        // creates a random string name for the file
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(logTAG, "Successfully uploaded image: $filename")
                } else {
                    Log.d(logTAG, "Upload image unsuccessful: $filename")
                }
            }
    }

    fun signup() {
        val name = binding.nameInputEditText.text.toString()
        val email = binding.emailInputEditText.text.toString()
        val password = binding.passwordInputEditText.text.toString()
        val confirmPassword = binding.confirmPasswordInputEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter an email and password", Toast.LENGTH_SHORT).show()
            return
        }

        if (password == confirmPassword) {
            // sign up
            setErrorTextField(false)
            // use requireActivity() instead of this since we are in a fragment, not an activity
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // move to new activity
                        Toast.makeText(requireActivity(), "Sign up successful", Toast.LENGTH_SHORT).show()
                        uploadImageToFirebaseStorage()
                    } else {
                        // show error
                        Toast.makeText(requireActivity(), "Error signing up occurred", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // return UI error
            setErrorTextField(true)
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



