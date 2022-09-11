package com.example.weebchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // login firebase user
        FirebaseHelper.loginUser(requireActivity(), email, password)
    }

    fun signup() {
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }
}