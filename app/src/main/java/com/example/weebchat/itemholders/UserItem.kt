package com.example.weebchat.itemholders

import com.example.weebchat.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class UserItem : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.new_message_list_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }

}