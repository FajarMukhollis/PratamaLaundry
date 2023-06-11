package com.fajar.pratamalaundry.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fajar.pratamalaundry.model.preference.UserPreference
import com.fajar.pratamalaundry.model.usecase.UserUseCase
import com.fajar.pratamalaundry.model.user.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreference: UserPreference
):ViewModel() {

    fun getUserData(): LiveData<UserModel> = userPreference.getUser().asLiveData()

    fun signOut() {
        viewModelScope.launch {
            userPreference.logout()
        }
    }
}