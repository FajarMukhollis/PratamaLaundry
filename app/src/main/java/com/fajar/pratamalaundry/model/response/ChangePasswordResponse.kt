package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("password")
    val password: String

)