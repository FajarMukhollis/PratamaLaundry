package com.fajar.pratamalaundry.model.remote

import com.fajar.pratamalaundry.model.request.NotificationRequest
import com.fajar.pratamalaundry.model.response.NotificationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiFCM {

    @POST("fcm/send")
    fun sendNotification(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authKey: String,
        @Body notification: NotificationRequest
    ): Call<NotificationResponse>
}