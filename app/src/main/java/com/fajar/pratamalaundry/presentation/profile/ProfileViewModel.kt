package com.fajar.pratamalaundry.presentation.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fajar.pratamalaundry.model.preference.UserPreference
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.DataProfile
import com.fajar.pratamalaundry.model.response.ProfileResponse
import com.fajar.pratamalaundry.model.usecase.UserUseCase
import com.fajar.pratamalaundry.model.user.UserModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ProfileViewModel(
    private val userPreference: UserPreference
):ViewModel() {

    suspend fun getToken() = userPreference.getToken().first()


    fun signOut() {
        viewModelScope.launch {
            userPreference.logout()
        }
    }
}