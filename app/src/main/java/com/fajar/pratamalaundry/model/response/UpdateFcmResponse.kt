package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class UpdateFcmResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)