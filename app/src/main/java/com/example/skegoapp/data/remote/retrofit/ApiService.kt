package com.example.skegoapp.data.remote.retrofit

import com.example.skegoapp.data.pref.LoginRequest
import com.example.skegoapp.data.pref.RegisterRequest
import com.example.skegoapp.data.pref.Routine
import com.example.skegoapp.data.remote.response.LoginResponse
import com.example.skegoapp.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("users/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("users/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("routines")
    fun getRoutines(): Call<List<Routine>>

    // Menambah rutinitas baru
    @POST("routines")
    fun addRoutine(@Body routine: Routine): Call<Routine>

    // Mendapatkan detail rutinitas berdasarkan ID
    @GET("routines/{id}")
    fun getRoutineById(@Path("id") id: Int): Call<Routine>

    // Menghapus rutinitas berdasarkan ID
    @DELETE("routines/{id}")
    fun deleteRoutine(@Path("id") id: Int): Call<Void>
}
