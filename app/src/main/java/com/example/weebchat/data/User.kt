package com.example.weebchat.data

/**
 * Holds data for a single user
 */
class User {
    var name: String? = null
    var uid: String? = null
    var profileImageUrl: String? = null

    constructor() : this("", "", "")

    constructor(name: String, uid: String, profileImageUrl: String) {
        this.name = name
        this.uid = uid
        this.profileImageUrl = profileImageUrl
    }
}