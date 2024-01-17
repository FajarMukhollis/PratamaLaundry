package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ArrayList<Transaksi>
) {
    data class Transaksi(
        @SerializedName("id_transaksi")
        val id_transaksi: String,

        @SerializedName("id_pelanggan")
        val id_pelanggan: String,

        @SerializedName("id_petugas")
        val id_petugas: String,

        @SerializedName("id_kategori")
        val id_kategori: String,

        @SerializedName("id_produk")
        val id_produk: String,

        @SerializedName("no_pesanan")
        val no_pesanan: String,

        @SerializedName("berat")
        val berat: String,

        @SerializedName("total_harga")
        val total_harga: String,

        @SerializedName("status_bayar")
        val status_bayar: String,

        @SerializedName("status_barang")
        val status_barang: String,

        @SerializedName("tgl_order")
        val tgl_order: String,

        @SerializedName("tgl_selesai")
        val tgl_selesai: String,

        @SerializedName("komplen")
        val komplen: String,

        @SerializedName("bukti_bayar")
        val bukti_bayar: String,

        @SerializedName("nama_produk")
        val nama_produk: String,

        @SerializedName("jenis_kategori")
        val jenis_kategori: String
    )
}

