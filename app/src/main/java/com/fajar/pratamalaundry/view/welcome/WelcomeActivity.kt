package com.fajar.pratamalaundry.view.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.databinding.ActivityWelcomeBinding
import com.fajar.pratamalaundry.view.login.LoginActivity
import com.fajar.pratamalaundry.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var _binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        supportActionBar?.hide()

        val btnGetStart: Button = findViewById(R.id.btn_GetStart)
        btnGetStart.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            _binding.btnGetStart.id -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}