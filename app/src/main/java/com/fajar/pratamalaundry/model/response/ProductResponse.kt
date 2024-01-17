package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
	@SerializedName("status")
	val status: Boolean,

	@SerializedName("message")
	val message: String,

	@SerializedName("data")
	val `data`: ArrayList<Product>
) {
	data class Product(
		@SerializedName("id_produk")
		val id_product: String,

		@SerializedName("id_kategori")
		val id_kategori: String,

		@SerializedName("jenis_kategori")
		val jenis_kategori: String,

		@SerializedName("nama_produk")
		val nama_produk: String,

		@SerializedName("durasi")
		val durasi: String,

		@SerializedName("harga_produk")
		val harga_produk: String,

		@SerializedName("satuan")
		val satuan: String
	)
}


