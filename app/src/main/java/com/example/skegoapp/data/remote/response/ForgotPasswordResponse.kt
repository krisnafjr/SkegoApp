package com.example.skegoapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(

	@field:SerializedName("message")
	val message: String? = null
)