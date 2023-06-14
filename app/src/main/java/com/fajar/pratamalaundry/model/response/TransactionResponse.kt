package com.fajar.pratamalaundry.model.response

data class TransactionResponse(
    val `data`: Data,
    val status: Boolean
)
data class Data(
    val berat: String,
    val id_produk: String,
    val total_harga: String
)