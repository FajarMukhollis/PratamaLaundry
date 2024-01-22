package com.fajar.pratamalaundry.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.model.response.HistoryResponse
import com.fajar.pratamalaundry.presentation.main.ChangeStatusBarangViewModel
import com.fajar.pratamalaundry.presentation.transaction.TransactionActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private lateinit var changeStatusBarangViewModel: ChangeStatusBarangViewModel
    private var previousTransactions: List<HistoryResponse.Transaksi> = emptyList()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val result = changeStatusBarangViewModel.getTransactions().value

            if (result != null && result.data.isNotEmpty()) {
                val newTransactions = result.data
                val addedTransactions = newTransactions.filter { it !in previousTransactions }

                if (addedTransactions.isNotEmpty()) {
                    val pendingConfirmationTransactions =
                        addedTransactions.filter { it.status_barang == "Menunggu Konfirmasi" }

                    if (pendingConfirmationTransactions.isNotEmpty()) {
                        showNotification()
                    }
                }

                previousTransactions = newTransactions
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification() {
        val notificationIntent = Intent(applicationContext, TransactionActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0,
            notificationIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "Pesanan Baru" // Sesuaikan dengan judul notifikasi yang Anda inginkan
        val body = "Ada pesanan baru yang harus anda konfirmasi." // Sesuaikan dengan isi notifikasi yang Anda inginkan

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "default")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

}