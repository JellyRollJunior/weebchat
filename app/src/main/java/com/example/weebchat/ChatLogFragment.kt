package com.example.weebchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.weebchat.databinding.FragmentChatLogBinding
import com.example.weebchat.itemholders.ChatFromItem
import com.example.weebchat.itemholders.ChatToItem
import com.xwray.groupie.GroupieAdapter

class ChatLogFragment : Fragment() {

    private lateinit var binding: FragmentChatLogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_log, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupieAdapter()
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        binding.chatLogRv.adapter = adapter
    }
}