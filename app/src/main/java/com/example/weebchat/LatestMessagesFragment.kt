package com.example.weebchat

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weebchat.databinding.FragmentLatestMessagesBinding
import com.example.weebchat.helpers.FirebaseHelper

class LatestMessagesFragment : Fragment() {

    private val logTag = "Latest Messages Fragment"
    private lateinit var binding: FragmentLatestMessagesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_latest_messages, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.latestMessagesFragment = this
        setHasOptionsMenu(true)

        if (!FirebaseHelper.isLoggedIn()) {
            findNavController().navigate(R.id.action_latestMessagesFragment_to_loginFragment)
        }
    }




    // app bar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu_btn -> {
                FirebaseHelper.signOut()
                findNavController().navigate(R.id.action_latestMessagesFragment_to_loginFragment)
                true
            }
            R.id.new_message_menu_btn -> {
                findNavController().navigate(R.id.action_latestMessagesFragment_to_newMessageFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}