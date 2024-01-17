package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
	@SerializedName("data")
	val data: Data,

	@SerializedName("message")
	val message: String,

	@SerializedName("status")
	val status: Boolean
) {
	data class Data(

		@SerializedName("id_kategori")
		val idKategori: String,

		@SerializedName("id_produk")
		val idProduk: String,

		@SerializedName("service")
		val service: String,

		@SerializedName("berat")
		val berat: String,

		@SerializedName("alamat_pelanggan")
		val alamatPelanggan: String,

		@SerializedName("total_harga")
		val totalHarga: Int

	)
}



