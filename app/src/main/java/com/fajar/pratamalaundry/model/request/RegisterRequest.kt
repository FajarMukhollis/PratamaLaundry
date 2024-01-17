package com.fajar.pratamalaundry.model.request

data class RegisterRequest(
    val nama_pelanggan: String,
    val no_telp: String,
    val email: String,
    val password: String
)