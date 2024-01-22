package com.fajar.pratamalaundry.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fajar.pratamalaundry.model.preference.UserPreference
import kotlinx.coroutines.flow.first

class MainViewModel(
    private val userPreference: UserPreference
): ViewModel() {

    fun getNama() = userPreference.getUser().asLiveData()

    suspend fun getToken() = userPreference.getToken().first()

    suspend fun saveTokenFcm(token: String) {
        userPreference.saveTokenFcm(token)
    }
    suspend fun getTokenFcm() : String {
        return userPreference.getTokenFcm()
    }

}