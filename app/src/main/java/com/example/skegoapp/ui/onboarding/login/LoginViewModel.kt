package com.example.skegoapp.ui.onboarding.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skegoapp.data.remote.repository.AuthRepository
import com.example.skegoapp.data.pref.UserModel
import com.example.skegoapp.data.pref.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPreference: UserPreference,
    private val authRepository: AuthRepository
) : ViewModel() {

    val loginStatus = MutableLiveData<String>()  // Untuk status login (success/error)
    val isLoading = MutableLiveData<Boolean>()   // Untuk menandakan loading state

    // Fungsi untuk melakukan login
    fun login(email: String, password: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = authRepository.loginUser(email, password)

                if (response.message == "Login successful") {
                    val user = UserModel(
                        email = email,
                        username = response.user.username,
                        userId = response.user.userId,  // userId as Int
                        isLogin = true
                    )

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
