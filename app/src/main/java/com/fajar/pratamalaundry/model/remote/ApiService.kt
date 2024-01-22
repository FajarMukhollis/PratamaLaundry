package com.fajar.pratamalaundry.model.remote

import com.fajar.pratamalaundry.model.request.*
import com.fajar.pratamalaundry.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @GET("user/history")
    fun getHistory(
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

    @GET("user/detail_history/{id_transaksi}")
    fun getDetailHistory(
        @Header("Authorization") token: String,
        @Path("id_transaksi") idTransaksi: String
    ): Call<DetailHistoryResponse>

    @FormUrlEncoded
    @POST("user/transaksi")
    fun postTransaction(
        @Header("Authorization") token: String,
        @Field("id_kategori") id_kategori: String,
        @Field("id_produk") id_produk: String,
        @Field("service") service: String,
        @Field("berat") berat: String,
        @Field("alamat_pelanggan") alamat_pelanggan: String,
        @Field("total_harga") total_harga: Int
    ): Call<TransactionResponse>

    @POST("user/complaint")
    fun postComplaint(
        @Header("Authorization") token: String,
        @Body complaintRequest: ComplaintRequest
    ): Call<ComplaintResponse>

    @Multipart
    @POST("user/confirm_payment")
    fun postPayment(
        @Part("id_transaksi") idTransaksi: RequestBody,
        @Part bukti_bayar: MultipartBody.Part
    ): Call<PaymentResponse>

    @GET("user/profile")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @POST("user/change_password")
    fun changePassword(
        @Header("Authorization") token: String,
        @Field("id_pelanggan") idPelanggan: String,
        @Field("old_password") oldPassword: String,
        @Field("new_password") newPassword: String
    ): Call<ChangePasswordResponse>

    @GET("get_rules_komplain")
    fun getRulesKomplain(): Call<RulesKomplainResponse>

    @GET("get_rules_asosiasi")
    fun getRulesAsosiasi(): Call<RulesAsosiasiResponse>

    @GET("get_category")
    fun getCategory(): Call<GetCategoryResponse>

    @GET("get_product_byIdCategory/{id_kategori}")
    fun getProductByIdCategory(
        @Path("id_kategori") idKategori: String
    ): Call<ProdukByIdKategoriResponse>

    @POST("user/update_fcm")
    fun updateFcm(
        @Body updateFcmRequest: UpdateFcmRequest
    ): Call<UpdateFcmResponse>

}