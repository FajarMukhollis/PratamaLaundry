package com.fajar.pratamalaundry.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fajar.pratamalaundry.model.remote.ApiService
import com.fajar.pratamalaundry.model.request.LoginRequest
import com.fajar.pratamalaundry.model.request.RegisterRequest
import com.fajar.pratamalaundry.model.response.LoginResponse
import com.fajar.pratamalaundry.model.response.RegisterResponse
import com.fajar.pratamalaundry.model.result.Result
import com.fajar.pratamalaundry.model.user.UserRegister

class UserRepository(
    private val apiService: ApiService
) : IUserRepository {
    override fun loginUser(email: String, pass: String,  fcm: String): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = apiService.loginUser(LoginRequest(email, pass, fcm))

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == true && responseBody.data != null) {
                        emit(Result.Success(responseBody))
                    } else {
                        emit(Result.Error("Email dan Password Tidak diketahui"))
                    }
                } else {
                    emit(Result.Error("Gagal melakukan permintaan login"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    override fun registerUser(
        nama_pelanggan: String,
        no_telp: String,
        email: String,
        password: String
    ): LiveData<Result<UserRegister>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.registerUser(
                RegisterRequest(
                    nama_pelanggan,
                    no_telp,
                    email,
                    password
                )
            )

            if (response.body()?.status.toBoolean()) {
                val data = UserRegister(
                    response.body()?.data?.nama_pelanggan.toString(),
                    response.body()?.data?.no_telp.toString(),
                    response.body()?.data?.email.toString(),
                    response.body()?.data?.password.toString()
                )
                emit(Result.Success(data))
            } else {
                emit(Result.Error("Email has been registered"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }


}