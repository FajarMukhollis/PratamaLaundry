package com.fajar.pratamalaundry.view.register

import androidx.lifecycle.ViewModel
import com.fajar.pratamalaundry.model.usecase.UserUseCase

class RegisterViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    fun registerUser(
        nama_pelanggan: String,
        no_telp: String,
        alamat: String,
        email: String,
        password: String
    ) = userUseCase.registerUser(
        nama_pelanggan, no_telp, alamat, email, password
    )
}