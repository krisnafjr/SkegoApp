package com.example.skegoapp.data.remote.retrofit

import com.example.skegoapp.data.pref.LoginRequest
import com.example.skegoapp.data.pref.RegisterRequest
import com.example.skegoapp.data.remote.response.LoginResponse
import com.example.skegoapp.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}
