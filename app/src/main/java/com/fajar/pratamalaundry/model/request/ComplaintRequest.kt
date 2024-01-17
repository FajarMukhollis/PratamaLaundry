package com.fajar.pratamalaundry.model.request

import com.google.gson.annotations.SerializedName

data class ComplaintRequest(
    @SerializedName("id_pelanggan")
    var id_pelanggan: String,

    @SerializedName("id_transaksi")
    var id_transaksi: String,

    @SerializedName("komplen")
    var komplen: String
)