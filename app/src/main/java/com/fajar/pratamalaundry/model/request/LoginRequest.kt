package com.fajar.pratamalaundry.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    var email : String,

    @SerializedName("password")
    var password : String,

    @SerializedName("fcm_pelanggan")
    var fcm_pelanggan : String
)