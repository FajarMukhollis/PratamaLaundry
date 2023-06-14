package com.fajar.pratamalaundry.presentation.transaction

import androidx.lifecycle.ViewModel
import com.fajar.pratamalaundry.model.preference.UserPreference
import kotlinx.coroutines.flow.first

class TransactionViewModel(
    private val userPreference: UserPreference
): ViewModel() {

    suspend fun getToken() = userPreference.getToken().first()
}