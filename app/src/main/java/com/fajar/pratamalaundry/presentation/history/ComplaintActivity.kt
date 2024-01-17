package com.fajar.pratamalaundry.presentation.history

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.databinding.ActivityComplaintBinding
import com.fajar.pratamalaundry.databinding.DialogLoadingBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.request.ComplaintRequest
import com.fajar.pratamalaundry.model.response.ComplaintResponse
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ComplaintActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityComplaintBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var complainSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityComplaintBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        complainSpinner = _binding.spComplain

        val selectedComplain = resources.getStringArray(R.array.komplain)
        val complainAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            selectedComplain
        )

        complainSpinner.adapter = complainAdapter

        complainSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // Periksa apakah item yang dipilih adalah "Lainnya"
                val selectedValue = selectedComplain[position]
                val isLainnya = selectedValue.equals(getString(R.string.lainnya), ignoreCase = true)

                // Ubah visibilitas TextInputLayout berdasarkan item yang dipilih
                _binding.tvComplaint.isVisible = !isLainnya
                _binding.etComplaint.isVisible = isLainnya
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        setViewModel()
        setActionBar()

        _binding.btnSubmit.setOnClickListener {
            postComplaint()
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

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]
    }

    private fun postComplaint() {
        lifecycleScope.launch {
            val stateLogin = historyViewModel.getToken()
            val etComplaint = _binding.etComplaint
            val selectedOption = _binding.spComplain.selectedItem.toString()
            val complaint =
                if (selectedOption.equals(getString(R.string.lainnya), ignoreCase = true)) {
                    etComplaint.text.toString()
                } else {
                    selectedOption
                }
            val idTransaksi = intent.getStringExtra("id_transaksi").toString()
            val idPelanggan = intent.getStringExtra("id_pelanggan").toString()

            when {
                complaint.isEmpty() -> {
                    if (selectedOption.equals(getString(R.string.lainnya), ignoreCase = true)) {
                        etComplaint.error = "Complaint tidak boleh kosong"
                    } else {
                        // Handle case when user selects an option other than "Lainnya" and leaves it empty
                        Toast.makeText(
                            this@ComplaintActivity,
                            "Anda memilih: $complaint",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                else -> {
                    val customBind = DialogLoadingBinding.inflate(layoutInflater)
                    val dialogLoadingBuilder = AlertDialog.Builder(this@ComplaintActivity).apply {
                        setView(customBind.root)
                        setCancelable(false)
                    }
                    val dialogLoading = dialogLoadingBuilder.create()

                    val retroInstance = ApiConfig.getApiService()
                    val call = retroInstance.postComplaint(
                        "Bearer: $stateLogin",
                        ComplaintRequest(
                            idPelanggan,
                            idTransaksi,
                            complaint
                        )
                    )

                    call.enqueue(object : Callback<ComplaintResponse> {
                        override fun onResponse(
                            call: Call<ComplaintResponse>,
                            response: Response<ComplaintResponse>
                        ) {
                            val message = response.body()?.message
                            if (response.isSuccessful) {
                                dialogLoading.dismiss()
                                Toast.makeText(this@ComplaintActivity, message, Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            } else {
                                dialogLoading.dismiss()
                                Toast.makeText(this@ComplaintActivity, message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<ComplaintResponse>, t: Throwable) {
                            dialogLoading.dismiss()
                            Toast.makeText(
                                this@ComplaintActivity,
                                "Gagal Mengirimkan Complaint",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        }
    }

}