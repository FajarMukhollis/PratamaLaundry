package com.fajar.pratamalaundry.presentation.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Surface
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fajar.pratamalaundry.databinding.ActivitySplashBinding
import com.fajar.pratamalaundry.presentation.login.LoginActivity
import com.fajar.pratamalaundry.presentation.main.MainActivity
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var _binding: ActivitySplashBinding
    private lateinit var splashViewModel: SplashViewModel
    private var isLogin = false
    private var splashTime = 2500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        supportActionBar?.hide()
        setViewModel()

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLogin) toMain() else toLogin()
        }, splashTime)
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        splashViewModel = ViewModelProvider(this, factory)[SplashViewModel::class.java]
        splashViewModel.getIsLogin().observe(this) {
            isLogin = it

        }
    }
}