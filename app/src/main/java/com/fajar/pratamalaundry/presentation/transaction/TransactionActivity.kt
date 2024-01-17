package com.fajar.pratamalaundry.presentation.transaction

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.databinding.ActivityTransactionBinding
import com.fajar.pratamalaundry.databinding.DialogLoadingBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.GetCategoryResponse
import com.fajar.pratamalaundry.model.response.ProductResponse
import com.fajar.pratamalaundry.model.response.ProdukByIdKategoriResponse
import com.fajar.pratamalaundry.model.response.TransactionResponse
import com.fajar.pratamalaundry.presentation.adapter.CategorySpinnerAdapter
import com.fajar.pratamalaundry.presentation.adapter.ProductSpinnerAdapter
import com.fajar.pratamalaundry.presentation.history.HistoryActivity
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import kotlin.math.ceil

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private const val TAG = "TransactionActivity"

class TransactionActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityTransactionBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var productSpinner: Spinner
    private lateinit var categorySpinner : Spinner
    private lateinit var layananSpinner: Spinner
    private var selectedCategoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        productSpinner = _binding.spProduct
        categorySpinner = _binding.spCategory
        layananSpinner = _binding.spLayanan

        val selectedLayanan = resources.getStringArray(R.array.layanan)
        val statusLayananAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            selectedLayanan
        )
        layananSpinner.adapter = statusLayananAdapter

        setViewModel()
        showCategorySpinner()
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
        override fun afterTextChanged(s: Editable?) {
            Log.d(TAG, "afterTextChanged: ")
            val selectedProduct = productSpinner.selectedItem as? ProdukByIdKategoriResponse.DataProduk
            val etBerat = _binding.etBerat.text.toString().replace(',', '.').toFloatOrNull()
            val harga = selectedProduct?.hargaProduk?.toIntOrNull() ?: 0
            val beratBulat = etBerat?.let { ceil(it * 2) / 2 }?.toFloat() ?: 0f
            val totalHarga = (beratBulat * harga).toInt()

            val formattedTotalHarga = NumberFormat.getCurrencyInstance().format(totalHarga)
            _binding.tvPrice.text = formattedTotalHarga
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Kosongkan, tidak digunakan pada contoh ini
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Kosongkan, tidak digunakan pada contoh ini
        }
    }

    private fun showProductSpinner(categoryId: String) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getProductByIdCategory(categoryId)
        call.enqueue(object : Callback<ProdukByIdKategoriResponse> {
            override fun onResponse(
                call: Call<ProdukByIdKategoriResponse>,
                response: Response<ProdukByIdKategoriResponse>
            ) {
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    val products = productResponse?.data ?: emptyList()

                    val adapter = ProductSpinnerAdapter(this@TransactionActivity, products)
                    productSpinner.adapter = adapter

                    productSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                 products[position]
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                Toast.makeText(
                                    this@TransactionActivity,
                                    "Kamu tidak memilih produk apapun",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@TransactionActivity,
                        "Gagal mendapatkan data produk",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProdukByIdKategoriResponse>, t: Throwable) {
                Log.e(TAG, "Gagal menampilkan data produk", t)
                Toast.makeText(
                    this@TransactionActivity,
                    "Gagal menampilkan data produk",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showCategorySpinner() {
        val apiService = ApiConfig.getApiService()
        val call = apiService.getCategory()
        call.enqueue(object : Callback<GetCategoryResponse> {
            override fun onResponse(
                call: Call<GetCategoryResponse>,
                response: Response<GetCategoryResponse>
            ) {
                if (response.isSuccessful) {
                    val categoryResponse = response.body()
                    val category = categoryResponse?.data ?: emptyList()

                    val adapter = CategorySpinnerAdapter(this@TransactionActivity, category)
                    categorySpinner.adapter = adapter

                    categorySpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedCategory = category[position]
                                selectedCategoryId = selectedCategory.id_kategori.toInt()

                                // Tambahkan pemanggilan untuk memuat produk berdasarkan kategori
                                showProductSpinner(selectedCategoryId.toString())
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                Toast.makeText(
                                    this@TransactionActivity,
                                    "Kamu tidak memilih produk apapun",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@TransactionActivity,
                        "Gagal mendapatkan data produk",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetCategoryResponse>, t: Throwable) {
                Toast.makeText(
                    this@TransactionActivity,
                    "Gagal menampilkan data produk",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun postTransaction() {
        Log.d(TAG, "postTransaction: ")
        lifecycleScope.launch {
            val token = transactionViewModel.getToken()
            val selectedCategory = categorySpinner.selectedItem as GetCategoryResponse.DataCategory
            val categoryId = selectedCategory.id_kategori
            val selectedProduct = productSpinner.selectedItem as ProdukByIdKategoriResponse.DataProduk
            val productId = selectedProduct.idProduk
            val etBerat = _binding.etBerat.text.toString().replace(',', '.').toFloatOrNull()
            val beratBulat = etBerat?.let { ceil(it * 2) / 2 }?.toFloat() ?: 0f
            val alamat = _binding.etAlamat.text.toString()
            val harga = selectedProduct.hargaProduk.toIntOrNull() ?: 0
            val totalHarga = (beratBulat * harga).toInt()
            Log.d(TAG, "postTransaction: $token")
            Log.d(TAG, "selectedCategory: $categoryId")
            Log.d(TAG, "selectedProduct: $productId")
            Log.d(TAG, "beratBulat: $beratBulat")
            Log.d(TAG, "harga: $harga")
            Log.d(TAG, "totalHarga: $totalHarga")

            val selectedLayanan = layananSpinner.selectedItem as? String ?: ""

            Log.d(TAG, "layanan: $selectedLayanan")

            val apiService = ApiConfig.getApiService()
            val call = apiService.postTransaction(
                "Bearer $token",
                id_kategori = categoryId,
                id_produk = productId,
                service = selectedLayanan,
                berat = beratBulat.toString(),
                alamat_pelanggan = alamat,
                total_harga = totalHarga
            )

            call.enqueue(object : Callback<TransactionResponse> {
                override fun onResponse(
                    call: Call<TransactionResponse>,
                    response: Response<TransactionResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Toast.makeText(
                                this@TransactionActivity,
                                "Berhasil melakukan transaksi",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                            toHistory()
                        }
                    } else {
                        Toast.makeText(
                            this@TransactionActivity,
                            "Transaksi Gagal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    Toast.makeText(
                        this@TransactionActivity,
                        "Transaksi Tidak Dapat Dilakukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun toHistory() {
        val moveToHistory = Intent(this, HistoryActivity::class.java)
        startActivity(moveToHistory)
    }

}
