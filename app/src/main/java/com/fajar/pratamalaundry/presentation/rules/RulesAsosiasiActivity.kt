package com.fajar.pratamalaundry.presentation.rules

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajar.pratamalaundry.databinding.ActivityRulesAsosiasiBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.RulesAsosiasiResponse
import com.fajar.pratamalaundry.presentation.adapter.RulesAsosiasiAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RulesAsosiasiActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRulesAsosiasiBinding
    private lateinit var rulesAsosiasiAdapter: RulesAsosiasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRulesAsosiasiBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setActionBar()
        getDataRulesAsosiasi()
        initRecyclerView()

        val swipeRefresh = _binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            getDataRulesAsosiasi()
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
        rulesAsosiasiAdapter = RulesAsosiasiAdapter(arrayListOf())
        _binding.recyclerRules.apply {
            layoutManager = LinearLayoutManager(this@RulesAsosiasiActivity)
            val decoration = DividerItemDecoration(this@RulesAsosiasiActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            adapter = rulesAsosiasiAdapter
            setHasFixedSize(true)
        }
    }

    private fun getDataRulesAsosiasi() {
        val retroInstance = ApiConfig.getApiService()
        val call = retroInstance.getRulesAsosiasi()
        call.enqueue(object : Callback<RulesAsosiasiResponse> {
            override fun onResponse(
                call: Call<RulesAsosiasiResponse>,
                response: Response<RulesAsosiasiResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val dataRules = response.body()?.data
                    if (dataRules != null) {
                        rulesAsosiasiAdapter.setData(dataRules)
                    }
                    Toast.makeText(
                        this@RulesAsosiasiActivity,
                        "Data Berhasil DiTemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showLoading(false)
                    Toast.makeText(
                        this@RulesAsosiasiActivity,
                        "Data Tidak Ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<RulesAsosiasiResponse>, t: Throwable) {
                Toast.makeText(
                    this@RulesAsosiasiActivity,
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