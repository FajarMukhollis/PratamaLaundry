package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class GetCategoryResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ArrayList<DataCategory>
) {
    data class DataCategory(

        @SerializedName("id_kategori")
        val id_kategori: String,

        @SerializedName("jenis_kategori")
        val jenis_kategori: String
    )
}