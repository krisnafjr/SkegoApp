package com.example.skegoapp.data.pref

data class ForgotPasswordRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String
)
