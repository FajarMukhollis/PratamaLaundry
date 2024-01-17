package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class PaymentResponse(
	@SerializedName("status")
	val status: Boolean,

	@SerializedName("message")
	val message: String,

	@SerializedName("image_url")
	val imageUrl: String
)

