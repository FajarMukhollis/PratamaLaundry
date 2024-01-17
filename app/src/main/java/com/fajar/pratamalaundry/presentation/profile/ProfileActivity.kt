package com.fajar.pratamalaundry.presentation.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fajar.pratamalaundry.databinding.ActivityProfileBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.ProfileResponse
import com.fajar.pratamalaundry.presentation.history.ComplaintActivity
import com.fajar.pratamalaundry.presentation.history.DetailHistoryActivity
import com.fajar.pratamalaundry.presentation.login.LoginActivity
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setViewModel()
        getProfile()
        setActionBar()

//        _binding.btnPassword.setOnClickListener {
//            val moveChangePassword = Intent(this, ChangePasswordActivity::class.java)
//            startActivity(moveChangePassword)
//        }

        _binding.btnLogout.setOnClickListener {
            val moveLogin = Intent(this, LoginActivity::class.java)
            moveLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            profileViewModel.signOut()
            startActivity(moveLogin)
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
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun getProfile() {
        lifecycleScope.launch {
            val token = profileViewModel.getToken()
            val retroInstance = ApiConfig.getApiService()
            val call = retroInstance.getProfile("Bearer $token")
            call.enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()?.dataProfile
                        if (result != null) {
                            val idPelanggan = result.id_pelanggan
                            _binding.tvNama.text = result.nama_pelanggan
                            _binding.tvNoHp.text = result.no_telp
                            _binding.tvEmail.text = result.email

                            _binding.btnPassword.setOnClickListener {
                                val moveChangePassword = Intent(
                                    this@ProfileActivity,
                                    ChangePasswordActivity::class.java
                                )
                                moveChangePassword.putExtra("id_pelanggan", idPelanggan)
                                startActivity(moveChangePassword)
                            }
                            Log.d("ProfileActivity", "ID Pelanggan: $idPelanggan")
                            Toast.makeText(
                                this@ProfileActivity,
                                "Berhasil Memuat Data",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@ProfileActivity,
                                "Data Tidak Ditemukan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ProfileActivity,
                            "Gagal Memuat Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Periksa Jaringan Anda",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

}