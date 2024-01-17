package com.fajar.pratamalaundry.model.user

data class UserModel (
    val localid: Int,
    val email: String,
    val nohp: String,
    val nama: String,
    val token: String,
    val isLogin: Boolean
    )