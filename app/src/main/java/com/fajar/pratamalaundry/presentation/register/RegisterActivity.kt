package com.fajar.pratamalaundry.presentation.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fajar.pratamalaundry.databinding.ActivityRegisterBinding
import com.fajar.pratamalaundry.databinding.DialogLoadingBinding
import com.fajar.pratamalaundry.presentation.login.LoginActivity
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import com.fajar.pratamalaundry.model.result.Result


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        supportActionBar?.hide()
        setViewModel()

        _binding.btnRegister.setOnClickListener {
            registerAction()
        }
    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    private fun registerAction() {
        val et_nama_pelanggan = _binding.etName
        val et_no_telp = _binding.etNoHp
        val et_email = _binding.etEmail
        val et_password = _binding.etPassword

        val nama_pelanggan = _binding.etName.text.toString()
        val no_telp = _binding.etNoHp.text.toString()
        val email = _binding.etEmail.text.toString()
        val password = _binding.etPassword.text.toString()


        when {
            nama_pelanggan.isEmpty() -> et_nama_pelanggan.error = "Nama tidak boleh kosong"
            no_telp.isEmpty() -> et_no_telp.error = "No Telp tidak boleh kosong"
            email.isEmpty() -> et_email.error = "Email tidak boleh kosong"
            !checkEmailError(email) -> et_email.error = "Email tidak valid"
            password.isEmpty() -> et_password.error = " Password tidak boleh kosong"
            password.length < 8 -> et_password.error = "Password kurang dari 8 karakter"

            else -> {
                val customBind = DialogLoadingBinding.inflate(layoutInflater)
                val dialogLoadingBuilder = AlertDialog.Builder(this).apply {
                    setView(customBind.root)
                    setCancelable(false)
                }
                val dialogLoading = dialogLoadingBuilder.create()

                registerViewModel.registerUser(nama_pelanggan, no_telp, email, password)
                    .observe(this) { result ->
                        when (result) {
                            is Result.Loading -> dialogLoading.show()
                            is Result.Success -> {
                                dialogLoading.dismiss()
                                Toast.makeText(
                                    this, "Register Berhasil",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                toLogin()
                            }

                            is Result.Error -> {
                                dialogLoading.dismiss()
                                Toast.makeText(
                                    this, "Register Gagal, Email Telah Digunakan",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
            }
        }
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkEmailError(target: CharSequence): Boolean {
        return if (TextUtils.isEmpty(target)) false else android.util.Patterns.EMAIL_ADDRESS.matcher(
            target
        ).matches()
    }
}