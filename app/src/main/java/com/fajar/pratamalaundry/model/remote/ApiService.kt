package com.fajar.pratamalaundry.model.remote

import androidx.lifecycle.LiveData
import com.fajar.pratamalaundry.model.request.LoginRequest
import com.fajar.pratamalaundry.model.request.RegisterRequest
import com.fajar.pratamalaundry.model.response.HistoryResponse
import com.fajar.pratamalaundry.model.response.LoginResponse
import com.fajar.pratamalaundry.model.response.ProductResponse
import com.fajar.pratamalaundry.model.response.RegisterResponse
import com.fajar.pratamalaundry.model.response.TransactionResponse
import retrofit2.*
import retrofit2.http.*

interface ApiService {

    @POST("user/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("user/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @GET("get_product")
    fun getAllProduct(): Call<ProductResponse>

    @GET("history")
    fun getHistory(
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

    @FormUrlEncoded
    @POST("transaksi")
    fun postTransaction(
        @Header("Authorization") token: String,
        @Field("id_produk") id_produk : String,
        @Field("berat") berat: String,
        @Field("total_harga") total_harga: String
    ): Call<TransactionResponse>
}