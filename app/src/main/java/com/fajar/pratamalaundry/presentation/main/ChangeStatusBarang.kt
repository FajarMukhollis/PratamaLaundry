package com.fajar.pratamalaundry.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fajar.pratamalaundry.model.remote.ApiService
import com.fajar.pratamalaundry.model.response.HistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeStatusBarang (private val apiService : ApiService) {

    fun getHistory(): LiveData<HistoryResponse> {
        val result = MutableLiveData<HistoryResponse>()

        apiService.getHistory("Token:").enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    result.value = response.body()
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                // Handle failure
            }
        })

        return result
    }

}