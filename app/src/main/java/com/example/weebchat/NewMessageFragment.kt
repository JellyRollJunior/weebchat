package com.example.weebchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weebchat.databinding.FragmentNewMessageBinding
import com.example.weebchat.itemholders.UserItem
import com.xwray.groupie.GroupieAdapter

class NewMessageFragment : Fragment() {

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
        val adapter = GroupieAdapter()
        adapter.add(UserItem())
        adapter.add(UserItem())
        binding.newMessageRv.adapter = adapter
    }
}