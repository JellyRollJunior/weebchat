package com.example.weebchat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weebchat.data.User

/**
 * Stores current user data and chat recipient user data
 *     -> Data is fetched in LatestMessagesFragment so do not use ViewModel in LatestMessagesFragment
 *     -> Or else null data may occur
 *     -> Just use Firebase commands to get data
 */
class UserViewModel : ViewModel() {

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private val _otherUser = MutableLiveData<User>()
    val otherUser: LiveData<User> = _otherUser

    fun setCurrentUser(currentUser: User) {
        _currentUser.value = currentUser
    }

    fun setReceiver(receiver: User) {
        _otherUser.value = receiver
    }
}