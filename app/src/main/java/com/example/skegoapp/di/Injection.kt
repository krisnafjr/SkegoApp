package com.example.skegoapp.di

import android.content.Context
import com.example.skegoapp.data.remote.repository.AuthRepository
import com.example.skegoapp.data.remote.repository.UserRepository
import com.example.skegoapp.data.pref.UserPreference
import com.example.skegoapp.data.pref.dataStore
import com.example.skegoapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideApiConfig(): ApiConfig {
        return ApiConfig
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref =
            UserPreference.getInstance(context.dataStore)
        val apiConfig = provideApiConfig()
        return UserRepository.getInstance(pref, apiConfig)
    }

    fun provideAuthRepository(): AuthRepository {
        return AuthRepository(ApiConfig.getApiService())
    }
}


