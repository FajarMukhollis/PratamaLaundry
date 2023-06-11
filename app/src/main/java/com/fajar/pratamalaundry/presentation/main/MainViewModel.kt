package com.fajar.pratamalaundry.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fajar.pratamalaundry.model.preference.UserPreference

class MainViewModel(
    private val adminPreference: UserPreference
): ViewModel() {

    fun getNama() = adminPreference.getUser().asLiveData()

}