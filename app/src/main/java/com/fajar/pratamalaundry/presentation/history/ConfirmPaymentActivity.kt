package com.fajar.pratamalaundry.presentation.history

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fajar.pratamalaundry.databinding.ActivityConfirmPaymentBinding
import com.fajar.pratamalaundry.model.remote.ApiConfig
import com.fajar.pratamalaundry.model.response.PaymentResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private const val TAG = "ComplaintActivity"

class ConfirmPaymentActivity : AppCompatActivity() {

    private var permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Izin akses galeri diterima.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Izin akses galeri ditolak.", Toast.LENGTH_SHORT).show()
//            openImagePicker()
        }
    }

    private lateinit var _binding: ActivityConfirmPaymentBinding
    private var selectedImage: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                selectedImage = it
                _binding.imgPayment.setImageURI(it)
                _binding.btnSubmit.isEnabled = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityConfirmPaymentBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.btnChooseImage.setOnClickListener {
            openImagePicker()
        }

        _binding.btnSubmit.setOnClickListener {
            if (selectedImage != null) {
                postImage()
            } else {
                Toast.makeText(this, "Masukkan Gambar Terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getContent.launch(arrayOf("image/*"))
            } else {
                requestPermissions()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getContent.launch(arrayOf("image/*"))
            } else {
                requestPermissions()
            }
        }
    }


    private fun convertUriToFile(uri: Uri): File? {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val extension =
            MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))
        val fileName = "image_${System.currentTimeMillis()}.$extension"
        val file = File(cacheDir, fileName)

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun postImage() {
        val idTransaksi = intent.getStringExtra("id_transaksi").toString()
        val idTransaksiRequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), idTransaksi)

        val image = selectedImage
        val imageFile = image?.let { convertUriToFile(it) }

        val imagePart: MultipartBody.Part? = imageFile?.let {
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), it)
            MultipartBody.Part.createFormData("bukti_bayar", it.name, requestFile)
        }

        if (imagePart != null) {
            val retroInstance = ApiConfig.getApiService()
            val call = retroInstance.postPayment(idTransaksiRequestBody, imagePart)
            call.enqueue(object : Callback<PaymentResponse> {
                override fun onResponse(
                    call: Call<PaymentResponse>,
                    response: Response<PaymentResponse>
                ) {
                    val paymentResponse = response.body()
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ConfirmPaymentActivity,
                            "Bukti pembayaran berhasil diunggah",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@ConfirmPaymentActivity,
                            "Bukti pembayaran gagal diunggah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ConfirmPaymentActivity,
                        "Tidak Dapat mengirimkan Bukti pembayaran",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        } else {
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestPermissions() {
        val permissions: Array<String> =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            } else {
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        if (!hasPermission(permissions[0])) {
            permissionLauncher.launch(permissions[0])
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult: $permissions")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE || requestCode == READ_MEDIA_IMAGES_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, buka galeri
                getContent.launch(arrayOf("image/*"))
            } else {
                // Izin ditolak, berikan pesan atau tindakan yang sesuai
                Toast.makeText(this, "Izin akses galeri ditolak.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 101
        private const val READ_MEDIA_IMAGES_PERMISSION_CODE = 102
    }

}