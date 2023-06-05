package com.fajar.pratamalaundry.view.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fajar.pratamalaundry.databinding.ActivityProfileBinding
import com.fajar.pratamalaundry.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.btnLogout.setOnClickListener {
            toLogin()
        }
    }

    private fun toLogin() {
        profileViewModel.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        profileViewModel.signOut()
        startActivity(intent)
    }
}