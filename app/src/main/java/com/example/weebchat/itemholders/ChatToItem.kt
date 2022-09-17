package com.example.weebchat.itemholders

import com.example.weebchat.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatToItem() : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.chat_to_row

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }


}