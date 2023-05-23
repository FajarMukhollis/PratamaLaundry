package com.fajar.pratamalaundry.view.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fajar.pratamalaundry.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }
}