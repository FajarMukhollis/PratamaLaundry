package com.fajar.pratamalaundry.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.request.UpdateFcmRequest
import com.fajar.pratamalaundry.model.response.UpdateFcmResponse
import com.fajar.pratamalaundry.presentation.history.HistoryActivity
import com.fajar.pratamalaundry.presentation.main.MainActivity
import com.fajar.pratamalaundry.presentation.main.MainViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationService : FirebaseMessagingService() {

    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val TAG = "PratamaLaundryFirebaseMessagingService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        remoteMessage.data.isNotEmpty().let {
            if (it) {
                val title = remoteMessage.data["title"]
                val body = remoteMessage.data["body"]
                val payloadData = remoteMessage.data

                if (payloadData.isNotEmpty()) {
                    if (payloadData["status_barang"] == "Sedang Di Proses" || payloadData["type"] == "order_status_update" ){
                        sendNotification(title, body)
                    } else if (payloadData["status_barang"] == "Selesai" || payloadData["type"] == "order_status_update"){
                        sendNotification(title, body)
                    } else {
                        Log.d(TAG, "Message data payload: ${remoteMessage.data}")
                    }
                }
            }
        }

        remoteMessage.notification?.let {
            sendNotification(it.title ?: "", it.body ?: "")
        }
    }

    override fun onNewToken(token: String) {
        // Perbarui token di server Anda jika diperlukan
        Log.d("FCM", "Refreshed token: $token")
        sendNewToken(token)
    }

    private fun sendNewToken(token: String) {
        val retroInstance = ApiConfig.getApiService()
        GlobalScope.launch {
            val idPetugas = mainViewModel.getNama().value?.localid
            val newTokenFCM = mainViewModel.getTokenFcm()
            val req = UpdateFcmRequest(idPetugas.toString(), newTokenFCM)
            val call = retroInstance.updateFcm(req)
            call.enqueue(object : Callback<UpdateFcmResponse> {
                override fun onResponse(
                    call: Call<UpdateFcmResponse>,
                    response: Response<UpdateFcmResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("FCM", "Token FCM berhasil diupdate")
                    } else {
                        Log.d("FCM", "Token FCM gagal diupdate")
                    }
                }

                override fun onFailure(call: Call<UpdateFcmResponse>, t: Throwable) {
                    Log.e("FCM", "Gagal mengirim token FCM", t)
                }
            })
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.action = "com.fajar.pratamalaundry.NOTIFICATION_CLICK"
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "default"
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pratama Laundry",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}