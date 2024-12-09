package com.example.skegoapp.ui.onboarding.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skegoapp.data.pref.ForgotPasswordRequest
import com.example.skegoapp.data.remote.repository.ForgotPasswordRepository
import com.example.skegoapp.data.remote.repository.UserRepository
import com.example.skegoapp.data.remote.response.ForgotPasswordResponse
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val repository: ForgotPasswordRepository) : ViewModel() {

    private val _passwordUpdateStatus = MutableLiveData<ForgotPasswordResponse?>()
    val passwordUpdateStatus: LiveData<ForgotPasswordResponse?> get() = _passwordUpdateStatus

    fun changePassword(email: String, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = repository.changePassword(ForgotPasswordRequest(email, oldPassword, newPassword))
                if (response.isSuccessful) {
                    _passwordUpdateStatus.postValue(response.body())
                } else {
                    _passwordUpdateStatus.postValue(null) // Handle failure
                }
            } catch (e: Exception) {
                _passwordUpdateStatus.postValue(null) // Handle error
            }
        }
    }
}

