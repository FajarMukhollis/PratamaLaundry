package com.fajar.pratamalaundry.presentation.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.fajar.pratamalaundry.databinding.ActivityTransactionBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.ProductResponse
import com.fajar.pratamalaundry.presentation.adapter.ProductAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityTransactionBinding
    private lateinit var productAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        showProduct()
    }

    private fun showProduct() {
        val retroInstance = ApiConfig.getApiService()
        val call = retroInstance.getAllProduct()
        call.enqueue(object: Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                val productSpinner = _binding.spService
                productAdapter = ArrayAdapter(this@TransactionActivity, android.R.layout.simple_spinner_item)
                productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                productSpinner.adapter = productAdapter

                fetchProduct()

            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@TransactionActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun fetchProduct() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val retroInstance = ApiConfig.getApiService()
                val call = retroInstance.getAllProduct()
                call.enqueue(object: Callback<ProductResponse> {
                    override fun onResponse(
                        call: Call<ProductResponse>,
                        response: Response<ProductResponse>
                    ) {
                            productAdapter.clear()
                            productAdapter.addAll()
                            productAdapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                        Toast.makeText(this@TransactionActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(this@TransactionActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun setupSpinner(product:ArrayList<ProductResponse.Product>,spinner: Spinner){
        val adapter = ProductAdapter(this,android.R.layout.simple_spinner_item, product)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedProduct = product[position]
                Toast.makeText(
                    this@TransactionActivity,
                    "Anda memilih ${selectedProduct.id_product}, ${selectedProduct.nama_produk} - ${selectedProduct.jenis_service} (Harga: ${selectedProduct.harga_produk})",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(
                    this@TransactionActivity,
                    "Tidak ada Item yang ada pilih",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}