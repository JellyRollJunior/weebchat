package com.example.weebchat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weebchat.data.User

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