package com.example.weebchat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weebchat.data.User

class ReceiverViewModel : ViewModel() {

    private val _receiver = MutableLiveData<User>()
    val receiver: LiveData<User> = _receiver

    fun setReceiver(receiver: User) {
        _receiver.value = receiver
    }
}