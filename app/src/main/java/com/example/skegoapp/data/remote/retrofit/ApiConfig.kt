package com.example.skegoapp.data.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private const val BASE_URL = "http://34.101.163.68:5000/api/users/"

    fun getApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY // Logs the request and response body

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Add logging to the client
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Use the configured client with logging
            .build()

        return retrofit.create(ApiService::class.java)
    }
}



