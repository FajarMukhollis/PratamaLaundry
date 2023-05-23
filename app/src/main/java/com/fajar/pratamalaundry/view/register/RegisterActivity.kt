package com.fajar.pratamalaundry.view.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fajar.pratamalaundry.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }
}