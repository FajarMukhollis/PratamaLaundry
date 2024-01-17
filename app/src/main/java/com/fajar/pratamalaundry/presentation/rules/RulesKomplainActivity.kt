package com.fajar.pratamalaundry.presentation.rules

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.databinding.ActivityRulesKomplainBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.RulesKomplainResponse
import com.fajar.pratamalaundry.presentation.adapter.RulesKomplainAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RulesKomplainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRulesKomplainBinding
    private lateinit var rulesKomplainAdapter: RulesKomplainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRulesKomplainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setActionBar()
        initRecyclerView()
        getDataRulesKomplain()

        val swipeRefresh = _binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            getDataRulesKomplain()
            swipeRefresh.isRefreshing = false
        }
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

    private fun initRecyclerView() {
        rulesKomplainAdapter = RulesKomplainAdapter(arrayListOf())
        _binding.recyclerRules.apply {
            layoutManager = LinearLayoutManager(this@RulesKomplainActivity)
            val decoration = DividerItemDecoration(this@RulesKomplainActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            adapter = rulesKomplainAdapter
            setHasFixedSize(true)
        }
    }

    private fun getDataRulesKomplain() {
        val retroInstance = ApiConfig.getApiService()
        val call = retroInstance.getRulesKomplain()
        call.enqueue(object : Callback<RulesKomplainResponse> {
            override fun onResponse(
                call: Call<RulesKomplainResponse>,
                response: Response<RulesKomplainResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val dataRules = response.body()?.data
                    if (dataRules != null) {
                        rulesKomplainAdapter.setData(dataRules)
                    }
                    Toast.makeText(
                        this@RulesKomplainActivity,
                        "Data Berhasil DiTemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showLoading(false)
                    Toast.makeText(
                        this@RulesKomplainActivity,
                        "Data Tidak Ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<RulesKomplainResponse>, t: Throwable) {
                Toast.makeText(
                    this@RulesKomplainActivity,
                    "Periksa Koneksi Internet Anda",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun showLoading(loading: Boolean) {
        _binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}