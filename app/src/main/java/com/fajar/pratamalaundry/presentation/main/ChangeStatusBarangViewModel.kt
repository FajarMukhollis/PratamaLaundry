package com.fajar.pratamalaundry.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fajar.pratamalaundry.model.response.HistoryResponse

class ChangeStatusBarangViewModel(
    private val changeStatusBarang: ChangeStatusBarang
) : ViewModel() {
    fun getTransactions(): LiveData<HistoryResponse> {
        return changeStatusBarang.getHistory()
    }
}