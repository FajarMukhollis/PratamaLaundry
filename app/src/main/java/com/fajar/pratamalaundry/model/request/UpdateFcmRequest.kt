package com.fajar.pratamalaundry.model.request

import com.google.gson.annotations.SerializedName

data class UpdateFcmRequest(
    @SerializedName("id_pelanggan")
    val id_pelanggan : String,

    @SerializedName("fcm_pelanggan")
    val fcm_pelanggan : String
)