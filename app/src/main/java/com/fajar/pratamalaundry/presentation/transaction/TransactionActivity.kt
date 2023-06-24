package com.fajar.pratamalaundry.presentation.transaction

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Spinner
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fajar.pratamalaundry.databinding.ActivityTransactionBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.ProductResponse
import com.fajar.pratamalaundry.model.response.TransactionResponse
import com.fajar.pratamalaundry.presentation.adapter.ProductSpinnerAdapter
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import kotlin.math.ceil

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class TransactionActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityTransactionBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setViewModel()
        showProductSpinner()
        setActionBar()

        _binding.etBerat.addTextChangedListener(textWatcher)

        _binding.btnOrder.setOnClickListener {
            postTransaction()
        }
    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        transactionViewModel = ViewModelProvider(this, factory)[TransactionViewModel::class.java]
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

    private val textWatcher = object : TextWatcher {
        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {
            val selectedProduct = spinner.selectedItem as ProductResponse.Product
            val etBerat = _binding.etBerat.text.toString().toFloatOrNull()
            val berat = ceil(etBerat?.toDouble() ?: 0.0).toInt().toString()
            val harga = selectedProduct.harga_produk.toInt()
            val totalHarga = (berat.toInt() * harga).toString()

            if (etBerat != null && etBerat > 0.0) {
                _binding.tvPrice.text = "Rp: $totalHarga"
            } else {
                _binding.tvPrice.text = "Rp: 0"
            }

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Kosongkan, tidak digunakan pada contoh ini
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Kosongkan, tidak digunakan pada contoh ini
        }

    }

    private fun calculatePrice(berat: String, harga: Int): String {
        val totalHarga = (berat.toInt() * harga).toString()

        return NumberFormat.getCurrencyInstance().format(totalHarga.toDouble())
    }

    private fun showProductSpinner() {
        spinner = _binding.spService
        val apiService = ApiConfig.getApiService()
        val call = apiService.getAllProduct()
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    val products = productResponse?.data ?: emptyList()
                    val adapter = ProductSpinnerAdapter(this@TransactionActivity, products)
                    spinner.adapter = adapter
                } else {
                    Toast.makeText(
                        this@TransactionActivity,
                        "Failed to fetch data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@TransactionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun postTransaction() {
        lifecycleScope.launch {
            val token = transactionViewModel.getToken()
            val selectedProduct = spinner.selectedItem as ProductResponse.Product
            val productId = selectedProduct.id_product
            val etBerat = _binding.etBerat.text.toString()
            val berat = ceil(etBerat.toDouble()).toInt().toString()
            val harga = selectedProduct.harga_produk.toInt()
            val totalHarga = calculatePrice(berat, harga)

            val apiService = ApiConfig.getApiService()
            val call = apiService.postTransaction(
                "Bearer $token",
                id_produk = productId,
                berat = berat,
                total_harga = totalHarga
            )

            call.enqueue(object : Callback<TransactionResponse> {
                override fun onResponse(
                    call: Call<TransactionResponse>,
                    response: Response<TransactionResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()
                        Toast.makeText(
                            this@TransactionActivity,
                            "Transaksi Berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@TransactionActivity,
                            "Transaksi Gagal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                }

                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    Toast.makeText(
                        this@TransactionActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
