package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class DetailHistoryResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: DetailDataHistory

) {
    data class DetailDataHistory(

        @SerializedName("id_transaksi")
        val idTransaksi: String,

        @SerializedName("id_pelanggan")
        val idPelanggan: String,

        @SerializedName("id_petugas")
        val idPetugas: String,

        @SerializedName("id_kategori")
        val idKategori: String,

        @SerializedName("id_produk")
        val idProduk: String,

        @SerializedName("no_pesanan")
        val no_pesanan: String,

        @SerializedName("service")
        val service: String,

        @SerializedName("berat")
        val berat: String,

        @SerializedName("alamat_pelanggan")
        val alamatPelanggan: String,

        @SerializedName("total_harga")
        val totalHarga: String,

        @SerializedName("status_bayar")
        val statusBayar: String,

        @SerializedName("status_barang")
        val statusBarang: String,

        @SerializedName("tgl_order")
        val tglOrder: String,

        @SerializedName("tgl_selesai")
        val tglSelesai: String,

        @SerializedName("komplen")
        val komplen: String,

        @SerializedName("bukti_bayar")
        val buktiBayar: Any,

        @SerializedName("nama_produk")
        val namaProduk: String,

        @SerializedName("jenis_kategori")
        val jenisKategori: String
    )
}
