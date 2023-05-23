package com.fajar.pratamalaundry.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fajar.pratamalaundry.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }
}