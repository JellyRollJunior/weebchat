package com.example.weebchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weebchat.data.User
import com.example.weebchat.databinding.FragmentNewMessageBinding
import com.example.weebchat.helpers.FirebaseHelper
import com.example.weebchat.itemholders.UserItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieAdapter

class NewMessageFragment : Fragment() {

    private val logTAG = "New Message Fragment"
    private lateinit var binding: FragmentNewMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_message, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newMessageFragment = this
        instantiateRecyclerView()
    }

    private fun instantiateRecyclerView() {
        val ref = FirebaseHelper.getUserRef()
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupieAdapter()
                // populate user data into adapter
                for (child in snapshot.children) {
                    Log.d(logTAG, child.toString())
                    val user = child.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                binding.newMessageRv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}