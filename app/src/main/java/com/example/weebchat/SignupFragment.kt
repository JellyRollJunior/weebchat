package com.example.weebchat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weebchat.databinding.FragmentSignupBinding
import com.example.weebchat.helpers.FirebaseHelper


class SignupFragment : Fragment() {

    private val logTAG = "Signup Fragment"
    private lateinit var binding: FragmentSignupBinding
    private var selectedPhotoUri: Uri? = null

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
        // honestly this is kinda hacky creating an intent to check if thers a gallery but not sure how to do this normally
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
            updateUi()
        }
    }

    private fun updateUi() {
        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedPhotoUri)
        binding.selectPhotoBtn.setImageBitmap(bitmap)
        binding.selectPhotoText.visibility = View.INVISIBLE
    }

    fun signup() {
        val name = binding.nameInputEditText.text.toString()
        val email = binding.emailInputEditText.text.toString()
        val password = binding.passwordInputEditText.text.toString()
        val confirmPassword = binding.confirmPasswordInputEditText.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedPhotoUri == null) {
            Toast.makeText(requireActivity(), "Please enter a name, email, password, and profile photo", Toast.LENGTH_SHORT).show()
            return
        } else if (password == confirmPassword) {
            setErrorTextField(false)
            // use requireActivity() instead of this since we are in a fragment, not an activity
            FirebaseHelper.createUser(requireActivity(), name, email, password, selectedPhotoUri)
            navigateToLatestMessages()
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

    private fun navigateToLatestMessages() {
        // clear activity stack

        // WAIT FOR DATABASE TO UPDATE... FIND A SOLUTION FOR THIS LOL
        Thread.sleep(3000)
        findNavController().navigate(R.id.action_signupFragment_to_latestMessagesFragment)
    }
}



