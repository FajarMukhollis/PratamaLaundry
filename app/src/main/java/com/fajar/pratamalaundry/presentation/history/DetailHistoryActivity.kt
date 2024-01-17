package com.fajar.pratamalaundry.presentation.history

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.databinding.ActivityDetailHistoryBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.DetailHistoryResponse
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDetailHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
//    private var baseUrl = "http://192.168.1.5/api-laundry"
    private var baseUrl = "https://pratamalaundry.my.id/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val idTransaksi = intent.getStringExtra("id_transaksi").toString()

        setSupportActionBar(_binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setViewModel()
        showData(idTransaksi)
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

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
    }

    private fun showData(id_transaksi:String) {
        lifecycleScope.launch {

            showLoading(true)
            val stateLogin = historyViewModel.getToken()
            val retroInstance = ApiConfig.getApiService()
            val call = retroInstance.getDetailHistory("Bearer $stateLogin", id_transaksi)

            call.enqueue(object : Callback<DetailHistoryResponse> {
                override fun onResponse(
                    call: Call<DetailHistoryResponse>,
                    response: Response<DetailHistoryResponse>
                ) {
                    if (response.isSuccessful) {
                        showLoading(false)
                        val detailHistory = response.body()?.data
                        detailHistory?.let {
                            _binding.tvNoPesanan.text = it.no_pesanan
                            _binding.tvAlamat.text = it.alamatPelanggan
                            _binding.tvKategori.text = it.jenisKategori
                            _binding.tvNameProduct.text = it.namaProduk
                            _binding.tvService.text = it.service
                            _binding.tvTanggalOrder.text = it.tglOrder
                            _binding.tvTanggalSelesai.text = it.tglSelesai
                            _binding.tvStatusBayar.text = it.statusBayar
                            _binding.tvStatusBarang.text = it.statusBarang
                            _binding.tvBerat.text = it.berat
                            _binding.tvHarga.text = it.totalHarga
                            _binding.tvKomplen.text = it.komplen
                            Glide.with(this@DetailHistoryActivity)
                                .load("$baseUrl/img_payment/${it.buktiBayar}")
                                .into(_binding.imgPayment)
                        }
                        Toast.makeText(
                            this@DetailHistoryActivity,
                            "Sukses Memuat Data",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        showLoading(false)
                        Toast.makeText(
                            this@DetailHistoryActivity,
                            "Gagal Memuat Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DetailHistoryResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(
                        this@DetailHistoryActivity,
                        "Gagal Memuat Data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    private fun showLoading(loading: Boolean) {
        _binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}