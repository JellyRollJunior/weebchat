package com.example.weebchat.itemholders

import android.widget.TextView
import com.example.weebchat.R
import com.example.weebchat.data.ChatMessage
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LatestMessageItem(private val message: ChatMessage) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.latest_message_list_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.latest_message_text_view).text = message.text
    }
}