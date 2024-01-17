package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val dataProfile: DataProfile

)

data class DataProfile(
    @SerializedName("id_pelanggan")
    val id_pelanggan: String,

    @SerializedName("id_transaksi")
    val id_transaksi: String,

    @SerializedName("nama_pelanggan")
    val nama_pelanggan: String,

    @SerializedName("no_telp")
    val no_telp: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String

)