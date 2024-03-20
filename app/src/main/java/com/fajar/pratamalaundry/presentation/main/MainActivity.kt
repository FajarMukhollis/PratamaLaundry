package com.fajar.pratamalaundry.presentation.main

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fajar.pratamalaundry.databinding.ActivityMainBinding
import com.fajar.pratamalaundry.presentation.history.HistoryActivity
import com.fajar.pratamalaundry.presentation.product.ProductActivity
import com.fajar.pratamalaundry.presentation.profile.ProfileActivity
import com.fajar.pratamalaundry.presentation.rules.RulesActivity
import com.fajar.pratamalaundry.presentation.transaction.TransactionActivity
import com.fajar.pratamalaundry.viewmodel.ViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var _binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        requestNotificationPermission()

        // Inisialisasi FCM
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCM", "Failed to get FCM token", task.exception)
                return@OnCompleteListener
            }
            // Token perangkat
            val token = task.result
            Log.d("FCM", "FCM Token (Pelanggan): $token")

            lifecycleScope.launch {
                mainViewModel.saveTokenFcm(token)
            }
        })

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setViewModel()
        getNameCustomer()

        if (intent.action == "com.fajar.pratamalaundry.NOTIFICATION_CLICK") {
            toHistory()
        }
        _binding.profile.setOnClickListener {
            toProfile()
        }
        _binding.conTransaction.setOnClickListener {
            toTransaction()
        }

        _binding.conService.setOnClickListener {
            toService()
        }

        _binding.conHistory.setOnClickListener {
            toHistory()
        }

        _binding.conRules.setOnClickListener {
            toRules()
        }
    }

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun requestNotificationPermission() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.areNotificationsEnabled()) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Izin Notifikasi Dibutuhkan")
                .setMessage("Aplikasi ini memerlukan izin notifikasi untuk memberikan informasi terbaru.")
                .setPositiveButton("Izinkan") { _, _ ->
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    startActivity(intent)
                }
                .setNegativeButton("Tolak") { _, _ ->

                }
                .show()
        }
    }

    private fun toHistory() {
        val moveToHistory = Intent(this, HistoryActivity::class.java)
        startActivity(moveToHistory)
    }

    private fun getNameCustomer() {
        mainViewModel.getNama().observe(this) { result ->
            if (result != null) {
                _binding.userName.text = result.nama
            }
        }
    }

    private fun toTransaction() {
        val moveToTransaction = Intent(this, TransactionActivity::class.java)
        startActivity(moveToTransaction)
    }

    private fun toService() {
        val moveToProduct = Intent(this, ProductActivity::class.java)
        startActivity(moveToProduct)
    }

    private fun toProfile() {
        val movetoProfile = Intent(this, ProfileActivity::class.java)
        startActivity(movetoProfile)
    }


    private fun toRules() {
        val moveToRules = Intent(this, RulesActivity::class.java)
        startActivity(moveToRules)
    }

}