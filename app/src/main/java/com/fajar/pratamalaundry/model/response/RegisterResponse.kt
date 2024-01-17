package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: DataRegis
    )

data class DataRegis(

    @SerializedName("nama_pelanggan")
    val nama_pelanggan: String,

    @SerializedName("no_telp")
    val no_telp: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)