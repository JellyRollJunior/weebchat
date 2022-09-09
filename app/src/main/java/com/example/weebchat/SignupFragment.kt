package com.example.weebchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.weebchat.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment() {

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

    fun signup() {
        val name = binding.nameInputEditText.text.toString()
        val email = binding.emailInputEditText.text.toString()
        val password = binding.passwordInputEditText.text.toString()
        val confirmPassword = binding.confirmPasswordInputEditText.text.toString()

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



