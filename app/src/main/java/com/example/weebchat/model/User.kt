package com.example.weebchat.model

class User {
    var name: String? = null
    var uid: String? = null
    var profileImageUrl: String? = null

    constructor() {}

    constructor(name: String, uid: String, profileImageUrl: String) {
        this.name = name
        this.uid = uid
        this.profileImageUrl = profileImageUrl
    }
}