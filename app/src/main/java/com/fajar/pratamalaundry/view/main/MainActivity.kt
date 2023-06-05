package com.fajar.pratamalaundry.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fajar.pratamalaundry.databinding.ActivityMainBinding
import com.fajar.pratamalaundry.view.product.ProductActivity
import com.fajar.pratamalaundry.view.profile.ProfileActivity
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setViewModel()
        getNameCustomer()
        _binding.profile.setOnClickListener {
            toProfile()
        }
        _binding.conTransaction.setOnClickListener {

        }

        _binding.conService.setOnClickListener {
            toService()
        }
    }

    private fun getNameCustomer() {
        mainViewModel.getNama().observe(this) {
            _binding.userName.text = it.nama
        }
    }

    private fun toService() {
        val moveToProduct = Intent(this, ProductActivity::class.java)
        startActivity(moveToProduct)
    }

    private fun toProfile() {
        val movetoProfile = Intent(this, ProfileActivity::class.java)
        movetoProfile.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(movetoProfile)
    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
}