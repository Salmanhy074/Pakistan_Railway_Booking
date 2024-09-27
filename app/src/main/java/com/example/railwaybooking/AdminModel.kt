package com.example.railwaybooking

class AdminModel {

    var email: String? = null
    var username: String? = null
    var phoneNumber: String? = null
    var licenseNumber: String? = null

    constructor()

    constructor(email: String, username: String, phoneNumber: String, licenseNumber: String) {
        this.email = email
        this.username = username
        this.phoneNumber = phoneNumber
        this.licenseNumber = licenseNumber

    }
}