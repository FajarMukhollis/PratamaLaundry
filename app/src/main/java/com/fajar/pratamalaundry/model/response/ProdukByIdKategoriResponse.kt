package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class ProdukByIdKategoriResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ArrayList<DataProduk>
) {
    data class DataProduk(
        @SerializedName("id_produk")
        val idProduk: String,

        @SerializedName("id_kategori")
        val idKategori: String,

        @SerializedName("jenis_kategori")
        val jenisKategori: String,

        @SerializedName("nama_produk")
        val namaProduk: String,

        @SerializedName("durasi")
        val durasi: String,

        @SerializedName("harga_produk")
        val hargaProduk: String,

        @SerializedName("satuan")
        val satuan: String
    )
}