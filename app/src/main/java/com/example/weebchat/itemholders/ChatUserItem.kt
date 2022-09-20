package com.example.weebchat.itemholders

import android.widget.ImageView
import android.widget.TextView
import com.example.weebchat.R
import com.example.weebchat.data.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatUserItem(val text: String, private val user: User) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.chat_user_row

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.user_text_view).text = text
        Picasso.get().load(user.profileImageUrl)
            .into(viewHolder.itemView.findViewById<ImageView>(R.id.user_image_view))
    }
}