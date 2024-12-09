package com.example.skegoapp.data.pref

data class UserModel(
    val email: String,
    val username: String,
    val userId: Int,
    val isLogin: Boolean = false
)

