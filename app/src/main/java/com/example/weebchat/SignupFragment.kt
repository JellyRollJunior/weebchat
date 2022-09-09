package com.example.weebchat

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
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
        val intent = Intent(Intent.ACTION_PICK)
            .setType("image/*")
        // launch gallery intent if there exists an gallery app
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            Log.d(logTAG, "Upload photo implicit intent launched")
            startForResult.launch(intent)
        }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                // Handle the Intent
                val uri = result.data!!.data
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)

                //do stuff here
                val bitmapDrawable = BitmapDrawable(bitmap)
                binding.test.setImageDrawable(bitmapDrawable)
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



