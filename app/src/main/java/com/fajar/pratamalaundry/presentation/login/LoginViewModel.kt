package com.fajar.pratamalaundry.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fajar.pratamalaundry.model.preference.UserPreference
import com.fajar.pratamalaundry.model.response.LoginResponse
import com.fajar.pratamalaundry.model.usecase.UserUseCase
import com.fajar.pratamalaundry.model.user.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userUseCase: UserUseCase,
    private val userPreference: UserPreference
): ViewModel() {

    fun loginUser(email: String, pass: String) = userUseCase.loginUser(email, pass)

    fun saveUser(userLogin: LoginResponse) {
        viewModelScope.launch {
            val data = UserModel(
                userLogin.data.id_pelanggan,
                userLogin.data.email,
                userLogin.data.alamat,
                userLogin.data.no_telp,
                userLogin.data.nama_pelanggan,
                userLogin.token,
                true
            )
            userPreference.saveUser(data)
        }
    }

    fun getToken() = userPreference.getToken().asLiveData()

    fun saveToken(token: String) {
        viewModelScope.launch {
            userPreference.saveToken(token)
        }
    }

}