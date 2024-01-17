package com.fajar.pratamalaundry.presentation.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.datastore.preferences.core.Preferences
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.databinding.ActivityLoginBinding
import com.fajar.pratamalaundry.databinding.DialogLoadingBinding
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import com.fajar.pratamalaundry.model.result.Result
import com.fajar.pratamalaundry.presentation.main.MainActivity
import com.fajar.pratamalaundry.presentation.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setViewModel()
        supportActionBar?.hide()
        _binding.btnLogin.setOnClickListener {
            loginAction()
        }

        _binding.btnRegister.setOnClickListener{
            toRegister()
        }

    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun loginAction() {
        val emailEditText = _binding.etEmail
        val passwordEditText = _binding.etPassword

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        when {
            email.isEmpty() -> emailEditText.error = resources.getString(R.string.empty_email)
            !checkEmailError(email) -> emailEditText.error = resources.getString(R.string.not_email)
            password.isEmpty() -> passwordEditText.error = resources.getString(R.string.empty_pass)
            password.length < 8 -> passwordEditText.error =
                resources.getString(R.string.pass_less_than_eight)
            else -> {
                //loading dialog
                val customBind = DialogLoadingBinding.inflate(layoutInflater)
                val loadingDialogBuilder = AlertDialog.Builder(this).apply {
                    setView(customBind.root)
                    setCancelable(false)
                }
                val loadingDialog = loadingDialogBuilder.create()

                loginViewModel.loginUser(email, password).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> loadingDialog.show()
                        is Result.Success -> {
                            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            loadingDialog.dismiss()
                            loginViewModel.saveUser(result.data)
                            Log.d(TAG, "loginAction: ${result.data}")
                            toMain()
                        }
                        is Result.Error -> {
                            loadingDialog.dismiss()
                            Toast.makeText(this, "Username dan Password Tidak diketahui", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "loginAction: ${result.error}")
                            errorAlert(result.error)
                        }
                    }
                }
            }

        }


    }

    private fun checkEmailError(target: CharSequence): Boolean {
        return if (TextUtils.isEmpty(target)) false else android.util.Patterns.EMAIL_ADDRESS.matcher(
            target
        ).matches()
    }

    private fun errorAlert(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Login Gagal")
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun toRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}