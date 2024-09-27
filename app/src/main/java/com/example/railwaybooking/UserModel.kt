package com.example.railwaybooking

class UserModel {
    var name: String? = null
    var email: String? = null
    var phone: String? = null

    constructor()

    constructor(name: String, email: String, phone: String) {
        this.name = name
        this.email = email
        this.phone = phone
    }
}
