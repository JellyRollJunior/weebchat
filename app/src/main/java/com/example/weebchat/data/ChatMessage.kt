package com.example.weebchat.data

/**
 * Holds data for a single chat message
 */
class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String,
                  val timestamp: Long) {
    constructor() : this("", "", "", "", -1)
}