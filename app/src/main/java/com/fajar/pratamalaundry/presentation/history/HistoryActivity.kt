package com.fajar.pratamalaundry.presentation.history

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajar.pratamalaundry.databinding.ActivityHistoryBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.HistoryResponse
import com.fajar.pratamalaundry.presentation.adapter.HistoryAdapter
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HistoryActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setViewModel()
        initRecyclerView()
        getHistory()
        setActionBar()

    }

    private fun setActionBar() {
        setSupportActionBar(_binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getHistory() {
        lifecycleScope.launch {
            showLoading(true)
            val token = historyViewModel.getToken()
            val retroInstance = ApiConfig.getApiService()
            val call = retroInstance.getHistory("Bearer $token")
            call.enqueue(object : Callback<HistoryResponse>{
                override fun onResponse(
                    call: Call<HistoryResponse>,
                    response: Response<HistoryResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        showData(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                    showLoading(false)
                }

            })
        }
    }


    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
    }

    private fun initRecyclerView() {
        historyAdapter = HistoryAdapter(arrayListOf())
        _binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            val decoration =
                DividerItemDecoration(this@HistoryActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            adapter = historyAdapter
            setHasFixedSize(true)
        }
    }


    private fun showData(data: HistoryResponse) {
        val results = data.transaksi
        historyAdapter.setData(results)

    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> _binding.progressBar.visibility = View.VISIBLE
            false -> _binding.progressBar.visibility = View.GONE
        }
    }
}