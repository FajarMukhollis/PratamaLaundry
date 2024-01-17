package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class RulesAsosiasiResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ArrayList<DataRulesAsosiasi>,
) {
    data class DataRulesAsosiasi(
        @SerializedName("id_rules_asosiasi")
        val idRulesAsosiasi: String,

        @SerializedName("id_petugas")
        val idPetugas: String,

        @SerializedName("aturan")
        val aturan: String

    )
}