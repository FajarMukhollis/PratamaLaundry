package com.fajar.pratamalaundry.presentation.register

import androidx.lifecycle.ViewModel
import com.fajar.pratamalaundry.model.usecase.UserUseCase

class RegisterViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    fun registerUser(
        nama_pelanggan: String,
        no_telp: String,
        email: String,
        password: String
    ) = userUseCase.registerUser(
        nama_pelanggan, no_telp, email, password
    )
}