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
    override fun loginUser(email: String, pass: String): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = apiService.loginUser(LoginRequest(email, pass))

                if (response.body()?.status == false) {
                    emit(Result.Error("salah di user repository"))
                } else if (response.body()?.status == true) {
                    emit(Result.Success(response.body()!!))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    override fun registerUser(
        nama_pelanggan: String,
        no_telp: String,
        alamat: String,
        email: String,
        password: String
    ): LiveData<Result<UserRegister>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.registerUser(
                RegisterRequest(
                    nama_pelanggan,
                    no_telp,
                    alamat,
                    email,
                    password
                )
            )

            if (response.body()?.status.toBoolean()) {
                val data = UserRegister(
                    response.body()?.data?.nama_pelanggan.toString(),
                    response.body()?.data?.no_telp.toString(),
                    response.body()?.data?.alamat.toString(),
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