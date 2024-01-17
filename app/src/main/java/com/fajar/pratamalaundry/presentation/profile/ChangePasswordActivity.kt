package com.fajar.pratamalaundry.presentation.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fajar.pratamalaundry.databinding.ActivityChangePasswordBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.ChangePasswordResponse
import com.fajar.pratamalaundry.presentation.login.LoginActivity
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityChangePasswordBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setViewModel()
        _binding.btnChangePassword.setOnClickListener {
            lifecycleScope.launch {
                changePassword()
            }
        }
    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this@ChangePasswordActivity, dataStore)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun loginAgain() {
        val moveLogin = Intent(this, LoginActivity::class.java)
        moveLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        profileViewModel.signOut()
        startActivity(moveLogin)
    }

    private fun changePassword() {
        lifecycleScope.launch {
            val token = profileViewModel.getToken()
            val reqOldPassword = _binding.etOldPassword.text.toString()
            val reqNewPassword = _binding.etNewPassword.text.toString()
            val reqConNewPassword = _binding.etConfirmNewPassword.text.toString()

            when {
                reqOldPassword.isEmpty() -> {
                    _binding.etOldPassword.error = "Password lama tidak boleh kosong"
                    _binding.etOldPassword.requestFocus()
                }

                reqNewPassword.isEmpty() -> {
                    _binding.etNewPassword.error = "Password baru tidak boleh kosong"
                    _binding.etNewPassword.requestFocus()
                }

                reqNewPassword.length < 8 -> {
                    _binding.etNewPassword.error = "Password baru minimal 8 karakter"
                    _binding.etNewPassword.requestFocus()
                }

                reqConNewPassword.isEmpty() -> {
                    _binding.etConfirmNewPassword.error =
                        "Konfirmasi Password baru tidak boleh kosong"
                    _binding.etConfirmNewPassword.requestFocus()
                }

                reqConNewPassword.length < 8 -> {
                    _binding.etConfirmNewPassword.error =
                        "Konfirmasi Password baru minimal 8 karakter"
                    _binding.etConfirmNewPassword.requestFocus()
                }

                reqNewPassword == reqOldPassword -> {
                    _binding.etNewPassword.error = "Gunakan Password yang berbeda"
                    _binding.etNewPassword.requestFocus()
                }

                reqNewPassword != reqConNewPassword -> {
                    _binding.etConfirmNewPassword.error = "Password tidak sesuai"
                    _binding.etConfirmNewPassword.requestFocus()
                }

                else -> {
                    val idPelanggan = intent.getStringExtra("id_pelanggan").toString()
                    Log.d("ChangePasswordActivity", "ID Pelanggan: $idPelanggan")
                    Log.d("ChangePasswordActivity", "Token: $token")
                    val retroInstance = ApiConfig.getApiService()
                    val call = retroInstance.changePassword(
                        "Token $token",
                        idPelanggan = idPelanggan,
                        oldPassword = reqOldPassword,
                        newPassword = reqNewPassword
                    )
                    call.enqueue(object : Callback<ChangePasswordResponse> {
                        override fun onResponse(
                            call: Call<ChangePasswordResponse>,
                            response: Response<ChangePasswordResponse>
                        ) {
                            if (response.isSuccessful) {
                                val changePasswordResponse = response.body()
                                if (changePasswordResponse?.status == true) {
                                    Toast.makeText(
                                        this@ChangePasswordActivity,
                                        "Berhasil mengubah password, Silakan Login kembali",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loginAgain()
                                }
                            } else {
                                Toast.makeText(
                                    this@ChangePasswordActivity,
                                    "Gagal mengubah password, Password Lama Tidak Sesuai",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        override fun onFailure(
                            call: Call<ChangePasswordResponse>,
                            t: Throwable
                        ) {
                            Toast.makeText(
                                this@ChangePasswordActivity,
                                "Terjadi kesalahan, periksa jaringan Anda",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
                }
            }
        }
    }
}