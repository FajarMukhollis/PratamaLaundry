package com.fajar.pratamalaundry.presentation.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fajar.pratamalaundry.model.preference.UserPreference


class SplashViewModel(
    private val pref: UserPreference
): ViewModel() {

    fun getIsLogin() = pref.getIsLogin().asLiveData()
}