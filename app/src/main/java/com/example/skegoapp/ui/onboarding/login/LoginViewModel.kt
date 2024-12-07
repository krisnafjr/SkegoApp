package com.example.skegoapp.ui.onboarding.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skegoapp.data.remote.repository.AuthRepository
import com.example.skegoapp.data.pref.UserModel
import com.example.skegoapp.data.pref.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val userPreference: UserPreference, private val authRepository: AuthRepository) : ViewModel() {

    val loginStatus = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun login(email: String, password: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = authRepository.loginUser(email, password)
                if (response.message == "Login successful") {
                    // Save the session after successful login
                    val user = UserModel(email, password, true)
                    userPreference.saveSession(user)

                    loginStatus.value = "success"
                } else {
                    loginStatus.value = "error"
                }
            } catch (e: Exception) {
                loginStatus.value = "error"
            } finally {
                isLoading.value = false
            }
        }
    }
}