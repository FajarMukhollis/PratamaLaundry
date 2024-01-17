package com.fajar.pratamalaundry.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fajar.pratamalaundry.model.preference.UserPreference
import kotlinx.coroutines.flow.first

class HistoryViewModel(
    private val userPreference: UserPreference
): ViewModel() {

    suspend fun getToken() = userPreference.getToken().first()
}