package com.fajar.pratamalaundry.model.response

data class TransaksiResponse(
    val `data`: DataTransaksi,
    val status: Boolean
)

data class DataTransaksi(
    val berat: String,
    val id_petugas: String,
    val id_produk: String,
    val tgl_order: String,
    val total_harga: String
)