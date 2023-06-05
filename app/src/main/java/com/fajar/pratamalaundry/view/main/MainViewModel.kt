package com.fajar.pratamalaundry.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fajar.pratamalaundry.model.preference.UserPreference
import kotlinx.coroutines.launch


class MainViewModel(
    private val userPreference: UserPreference
) : ViewModel() {

    fun getNama() = userPreference.getUser().asLiveData()

}