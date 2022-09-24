package com.example.weebchat.itemholders

import android.widget.ImageView
import android.widget.TextView
import com.example.weebchat.R
import com.example.weebchat.data.ChatMessage
import com.example.weebchat.data.User
import com.example.weebchat.helpers.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LatestMessageItem(private val message: ChatMessage) : Item<GroupieViewHolder>() {

    var user: User? = null

    override fun getLayout() = R.layout.latest_message_list_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.latest_message_text_view).text = message.text

        // fetch user id
        val chatPartnerId: String = if (message.fromId == FirebaseHelper.getUid()) {
            message.toId
        } else {
            message.fromId
        }

        // load user data into views
        val ref = FirebaseHelper.getUserRef(chatPartnerId)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)

                viewHolder.itemView.findViewById<TextView>(R.id.latest_name_text_view).text = user?.name
                Picasso.get().load(user?.profileImageUrl)
                    .into(viewHolder.itemView.findViewById<ImageView>(R.id.latest_image_view))
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}