package com.example.weebchat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weebchat.helpers.FirebaseHelper

class LatestMessagesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        if (!FirebaseHelper.isLoggedIn()) {
            findNavController().navigate(R.id.action_latestMessagesFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_messages, container, false)
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
                return true
            }
            R.id.new_message_menu_btn -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}