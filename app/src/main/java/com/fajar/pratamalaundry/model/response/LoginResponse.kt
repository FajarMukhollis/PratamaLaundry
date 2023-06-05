package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("data")
    val `data`: DataUser
)

data class DataUser(
    @SerializedName("id_pelanggan")
    val id_pelanggan: Int,

    @SerializedName("id_transaksi")
    val id_transaksi: Int,

    @SerializedName("nama_pelanggan")
    val nama_pelanggan: String,

    @SerializedName("no_telp")
    val no_telp: String,

    @SerializedName("alamat")
    val alamat: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String

)