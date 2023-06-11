package com.fajar.pratamalaundry.presentation.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fajar.pratamalaundry.databinding.ActivityProfileBinding
import com.fajar.pratamalaundry.presentation.login.LoginActivity
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setViewModel()
        showData()

        _binding.btnLogout.setOnClickListener {
            val moveLogin = Intent(this, LoginActivity::class.java)
            profileViewModel.signOut()
            startActivity(moveLogin)
        }

    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun showData(){
        profileViewModel.getUserData().observe(this){ result ->
            if (result != null){
                _binding.tvNama.text = result.nama
                _binding.tvNoHp.text = result.nohp
                _binding.tvAlamat.text  = result.alamat
                _binding.tvEmail.text = result.email
            }

        }
    }
}