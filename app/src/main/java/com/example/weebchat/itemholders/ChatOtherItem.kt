package com.example.weebchat.itemholders

import android.widget.TextView
import com.example.weebchat.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatOtherItem(val text: String) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.chat_other_row

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.from_text_view).text = text
    }
}