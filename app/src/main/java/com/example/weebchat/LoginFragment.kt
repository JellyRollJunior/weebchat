package com.example.weebchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weebchat.databinding.FragmentLoginBinding
import com.example.weebchat.helpers.FirebaseHelper

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginFragment = this
    }

    fun login() {
        val email: String = binding.emailInputEditText.text.toString()
        val password: String = binding.passwordInputEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (FirebaseHelper.loginUser(email, password)) {
                Toast.makeText(requireActivity(), "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Incorrect credentials entered!", Toast.LENGTH_SHORT).show()
            }
        }

        // basically navigation wasnt working because YOU GOTTA WAIT FOR FIREBASE AUTH TO UPDATE LOGIN
        // WE LOGIN THEN CHECK IMMEDIATELY THEN IT RETURNS THAT WERE NOT LOGGED IN -> NEED A SPECIAL FUNCTION TO TEST THIS
        // A WAIT FOR FIREBASE TO UPDATE OR WAIT FOR FIREBASE OPERATION TO FINISH ETC
        // SHOULD PROBABLY DISPLAY A LOADING SPINNER
        Thread.sleep(3000)
        findNavController().navigate(R.id.action_loginFragment_to_latestMessagesFragment)
    }

    fun signup() {
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }
}