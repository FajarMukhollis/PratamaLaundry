package com.fajar.pratamalaundry.view.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajar.pratamalaundry.databinding.ActivityProductBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.ProductResponse
import com.fajar.pratamalaundry.view.adapter.ListProductAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityProductBinding
    private lateinit var listProductAdapter: ListProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initRecyclerView()
        getDataFromApi()
    }

    private fun getDataFromApi() {
        showLoading(true)
        val retroInstance = ApiConfig.getApiService()
        val call = retroInstance.getAllProduct()
        call.enqueue(object: Callback<ProductResponse>{
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    showData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                showLoading(false)
            }

        })
    }

    private fun showData(data: ProductResponse) {
        val results = data.data
        listProductAdapter.setData(results)

    }

    private fun initRecyclerView() {
        listProductAdapter = ListProductAdapter(arrayListOf())
        _binding.rvProduct.apply {
            layoutManager = LinearLayoutManager(this@ProductActivity)
            val decoration = DividerItemDecoration(this@ProductActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            adapter = listProductAdapter
            setHasFixedSize(true)
        }
    }

    private fun showLoading(loading: Boolean) {
        when(loading) {
            true -> _binding.progressBar.visibility = View.VISIBLE
            false -> _binding.progressBar.visibility = View.GONE
        }
    }
}