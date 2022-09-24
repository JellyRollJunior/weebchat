package com.example.weebchat.itemholders

import android.widget.ImageView
import android.widget.TextView
import com.example.weebchat.R
import com.example.weebchat.data.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

/**
 * Recycler view list item for users in NEW MESSAGE FRAGMENT
 */
class UserItem(val user: User) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.new_message_list_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.name_text_view).text = user.name
        Picasso.get().load(user.profileImageUrl)
            .into(viewHolder.itemView.findViewById<ImageView>(R.id.profile_image_view))
    }
}