package com.fajar.pratamalaundry.presentation.main

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.fajar.pratamalaundry.databinding.ActivityMainBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.remote.ApiFirebase
import com.fajar.pratamalaundry.model.request.Notification
import com.fajar.pratamalaundry.model.request.NotificationRequest
import com.fajar.pratamalaundry.model.response.HistoryResponse
import com.fajar.pratamalaundry.model.response.NotificationResponse
import com.fajar.pratamalaundry.presentation.MyWorker
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val WORKER_TAG = "MyWorker"
    }

    private lateinit var _binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var changeStatusBarangViewModel: ChangeStatusBarangViewModel
    private var previousHistory: List<HistoryResponse.Transaksi> = emptyList()

    private val pollingHandler = Handler()
    private val pollingInterval = 10000

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

        val workRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            15, TimeUnit.MINUTES // Atur interval menjadi 3 menit
        ).addTag(WORKER_TAG).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORKER_TAG,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setViewModel()
        getNameCustomer()
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

    override fun onResume() {
        super.onResume()
        startPolling()
    }

    override fun onPause() {
        super.onPause()
        stopPolling()
    }

    private fun startPolling() {
        pollingHandler.post(object : Runnable {
            override fun run() {
                getHistory()
                pollingHandler.postDelayed(this, pollingInterval.toLong())
            }
        })
    }

    private fun stopPolling() {
        pollingHandler.removeCallbacksAndMessages(null)
    }

    private fun getHistory() {
        lifecycleScope.launch {
            val token = mainViewModel.getToken()
            val retroInstance = ApiConfig.getApiService()
            val call = retroInstance.getHistory("Bearer $token")
            call.enqueue(object : Callback<HistoryResponse> {
                override fun onResponse(
                    call: Call<HistoryResponse>,
                    response: Response<HistoryResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body().let {
                            val newDataChange = it?.data
                            val dataChange = newDataChange?.filter { it !in previousHistory }

                            val changeStatus =
                                dataChange?.filter { it.status_barang == "Selesai" || it.status_barang == "Sedang Di Proses" }
                            if (changeStatus?.isNotEmpty() == true) {
                                Log.d("NotificationDebug:", "Notifikasi akan muncul")

                                previousHistory = newDataChange.toList()
                                postNotification()

                            } else {
                                Log.d("NotificationDebug:", "Notifikasi tidak akan muncul")
                            }
                        }
                    } else {
                        Log.d("StatusBarang:", "Tidak Ada Perubahan Status Barang")
                    }
                }

                override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    private fun postNotification() {
        val retroInstance = ApiFirebase.getApiFirebase()
        lifecycleScope.launch {
            val serverKey =
                "AAAADRgdVUk:APA91bEWUU-SfVofYgWivzqc_971BtZXAnHEx9_aKPLAzMQiBa0ntRwlISevXQ-gg3vTKQoiIx61q7pDHNeaTPHmlRPvMIUnZJ58wF-v88SL6egdS3Qk9BKq2YWeIXxPJ24pjzruXsBs"
            val token = mainViewModel.getTokenFcm()
            val title = "Status Barang"
            val content = "Cek Sekarang!, Status Barang Anda Telah Berubah"
            val reqNotification = NotificationRequest(
                to = token,
                notification = Notification(
                    title = title,
                    body = content
                )
            )
            Log.d(TAG, "serverKey: $serverKey")
            Log.d(TAG, "FCM Token Post (Petugas): $token")
            val call = retroInstance.sendNotification(
                "application/json",
                "key=$serverKey",
                reqNotification
            )

            call.enqueue(object : Callback<NotificationResponse> {
                override fun onResponse(
                    call: Call<NotificationResponse>,
                    response: Response<NotificationResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()
                        Log.d(TAG, "serverKey: $serverKey")
                        Log.d(TAG, "FCM Token (Petugas): $token")

                    }
                }

                override fun onFailure(
                    call: Call<NotificationResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, "Fail serverKey: $serverKey")
                    Log.d(TAG, "Fail FCM Token (Petugas): $token")
                }

            })
        }
    }

    private fun observeTransactions() {
        changeStatusBarangViewModel.getTransactions().observe(this) { result ->
            if (result != null && result.data.isNotEmpty()) {
                val newStatusBarang = result.data
                val statusBarang = newStatusBarang.filter { it !in previousHistory }

                if (statusBarang.isNotEmpty()) {
                    val status =
                        statusBarang.filter { it.status_barang == "Selesai" }

                    if (status.isNotEmpty()) {
//                        showNotification()
                    }
                }
            }
        }
    }

//    private fun showNotification() {
//        val notificationIntent = Intent(this, NotificationService::class.java)
//        NotificationService.enqueueWork(this, notificationIntent)
//    }

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

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(this, dataStore)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun toRules() {
        val moveToRules = Intent(this, RulesActivity::class.java)
        startActivity(moveToRules)
    }

}