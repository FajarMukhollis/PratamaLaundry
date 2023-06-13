package com.fajar.pratamalaundry.presentation.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.fajar.pratamalaundry.databinding.ActivityTransactionBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.ProductResponse
import com.fajar.pratamalaundry.presentation.adapter.ProductSpinnerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityTransactionBinding
    private lateinit var spinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        showProductSpinner()
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

    private fun showProductSpinner() {
        spinner = _binding.spService
        val apiService = ApiConfig.getApiService()
        val call = apiService.getAllProduct()
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    val products = productResponse?.data ?: emptyList()
                    val adapter = ProductSpinnerAdapter(this@TransactionActivity, products)
                    spinner.adapter = adapter
                } else {
                    Toast.makeText(this@TransactionActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@TransactionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}