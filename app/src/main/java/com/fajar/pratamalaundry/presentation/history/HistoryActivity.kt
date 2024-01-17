package com.fajar.pratamalaundry.presentation.history

import android.content.Context
import android.content.Intent
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
import java.text.FieldPosition

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HistoryActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val swipeRefresh = _binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            getHistory()
            swipeRefresh.isRefreshing = false
        }

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
            call.enqueue(object : Callback<HistoryResponse> {
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
        historyAdapter.setOnItemClickListener(object : HistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val idTransaction = historyAdapter.getItem(position)
                val intent = Intent(this@HistoryActivity, DetailHistoryActivity::class.java)
                intent.putExtra("id_transaksi", idTransaction.id_transaksi)
                startActivity(intent)
            }
        })

        historyAdapter.setOnReportClickListener(object : HistoryAdapter.OnReportClickListener {
            override fun onReportClick(position: Int) {
                val idTransaction = historyAdapter.getItem(position)
                val intent = Intent(this@HistoryActivity, ComplaintActivity::class.java)
                intent.putExtra("id_pelanggan", idTransaction.id_pelanggan)
                intent.putExtra("id_transaksi", idTransaction.id_transaksi)
                startActivity(intent)
            }
        })

        historyAdapter.setOnPaymentClickListener(object : HistoryAdapter.OnPaymentClickListener {
            override fun onPaymentClick(position: Int) {
                val idTransaction = historyAdapter.getItem(position)
                val intent = Intent(this@HistoryActivity, ConfirmPaymentActivity::class.java)
                intent.putExtra("id_transaksi", idTransaction.id_transaksi)
                startActivity(intent)
            }
        })

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
        val results = data.data
        historyAdapter.setData(results)

        val dataEmpty = _binding.tvEmptyData

        if (results.isEmpty()) {
            dataEmpty.visibility = View.VISIBLE
        } else {
            dataEmpty.visibility = View.GONE
        }
    }

    private fun showLoading(loading: Boolean) {
        when (loading) {
            true -> _binding.progressBar.visibility = View.VISIBLE
            false -> _binding.progressBar.visibility = View.GONE
        }
    }
}