package com.fajar.pratamalaundry.model.usecase

import androidx.lifecycle.LiveData
import com.fajar.pratamalaundry.model.response.LoginResponse
import com.fajar.pratamalaundry.model.result.Result
import com.fajar.pratamalaundry.model.user.UserRegister

interface UserUseCase {
    fun loginUser(email: String, pass: String, fcm: String): LiveData<Result<LoginResponse>>

    fun registerUser(
        nama_pelanggan: String,
        no_telp: String,
        email: String,
        password: String
    ): LiveData<Result<UserRegister>>

}