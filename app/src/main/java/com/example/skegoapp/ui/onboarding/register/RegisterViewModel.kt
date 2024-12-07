package com.example.skegoapp.ui.onboarding.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skegoapp.data.remote.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val registerStatus = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun register(username: String, email: String, password: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = authRepository.registerUser(username, email, password)
                Log.d("RegisterViewModel", "Response: $response")

                // If response.userId is greater than 0, registration is successful
                if (response.userId > 0) {
                    registerStatus.value = "success"
                } else {
                    registerStatus.value = "error"
                }

            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Error during registration: ${e.message}")
                registerStatus.value = "error"
            } finally {
                isLoading.value = false
            }
        }
    }
}