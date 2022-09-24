package com.example.weebchat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.weebchat.data.ChatMessage
import com.example.weebchat.databinding.FragmentChatLogBinding
import com.example.weebchat.helpers.FirebaseHelper
import com.example.weebchat.itemholders.ChatOtherItem
import com.example.weebchat.itemholders.ChatUserItem
import com.example.weebchat.model.UserViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.xwray.groupie.GroupieAdapter

/**
 * Fragment which displays chat messages between current user and recipient
 */
class ChatLogFragment : Fragment() {

    private val logTAG = "Chat Log Fragment"
    private lateinit var binding: FragmentChatLogBinding
    private val sharedViewModel: UserViewModel by activityViewModels()
    private val adapter = GroupieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_log, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chatLogFragment = this

        setFragmentLabel()
        instantiateChatLog()
        scrollToBottom()
    }

    private fun instantiateChatLog() {
        val currentUserUid = sharedViewModel.currentUser.value?.uid
        val otherUserUid = sharedViewModel.otherUser.value?.uid

        // send to firebase storage
        if (!currentUserUid.isNullOrEmpty() && !otherUserUid.isNullOrEmpty() ) {
            val ref = FirebaseHelper.getMessagesRef(currentUserUid, otherUserUid)

            // listen for new messages
            ref.addChildEventListener(object: ChildEventListener {

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d(logTAG, snapshot.toString())
                    populateAdapterWithMessages(snapshot)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }
        binding.chatLogRv.adapter = adapter
    }

    private fun populateAdapterWithMessages(snapshot: DataSnapshot){
        val chatMessage = snapshot.getValue(ChatMessage::class.java)
        val currentUserUid = FirebaseHelper.getUid()
        val text = chatMessage?.text

        if (text != null) {
            if (chatMessage.fromId == currentUserUid) {
                adapter.add(0, ChatUserItem(text, sharedViewModel.currentUser.value!!))
            } else {
                adapter.add(0, ChatOtherItem(text, sharedViewModel.otherUser.value!!))
            }
        }
    }

    private fun setFragmentLabel() {
        val receiverName = sharedViewModel.otherUser.value?.name
        (activity as AppCompatActivity).supportActionBar?.title =
            receiverName ?: resources.getString(R.string.chat_log_title)
        Log.d(logTAG, "" + receiverName)
    }

    fun sendMessage() {
        // grab message from et
        val messageText = binding.textInput.text.toString()

        if (messageText.isNotBlank()) {
            val currentUserUid = sharedViewModel.currentUser.value?.uid
            val otherUserUid = sharedViewModel.otherUser.value?.uid

            // send to firebase storage
            if (!currentUserUid.isNullOrEmpty() && !otherUserUid.isNullOrEmpty()) {
                FirebaseHelper.saveMessage(messageText, currentUserUid, otherUserUid)
            }
        }
        clearEditText()
        scrollToBottom()
    }

    private fun clearEditText() {
        binding.textInput.text.clear()
    }

    private fun scrollToBottom() {
        binding.chatLogRv.scrollToPosition(0)
    }
}