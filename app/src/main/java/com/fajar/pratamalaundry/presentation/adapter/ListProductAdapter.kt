package com.fajar.pratamalaundry.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.model.response.ProductResponse

class ListProductAdapter(private val results : ArrayList<ProductResponse.Product>)
    : RecyclerView.Adapter<ListProductAdapter.ViewHolderProduct>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProduct {
        LayoutInflater.from(parent.context).inflate(R.layout.item_row_product, parent, false).apply {
            return ViewHolderProduct(this)
        }

    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: ViewHolderProduct, position: Int) {
        val result = results[position]
        holder.apply {
            result.id_kategori
            kategori.text = result.jenis_kategori
            nama_produk.text = result.nama_produk
            durasi.text = result.durasi
            harga_produk.text = result.harga_produk
            satuan.text = result.satuan
        }
    }

    inner class ViewHolderProduct(val view: View): RecyclerView.ViewHolder(view){
        val kategori: TextView = view.findViewById(R.id.tv_category)
        val nama_produk: TextView = view.findViewById(R.id.tv_produk)
        val durasi: TextView = view.findViewById(R.id.tv_durasi)
        val harga_produk: TextView = view.findViewById(R.id.tv_harga)
        val satuan: TextView = view.findViewById(R.id.tv_satuan)
    }

    fun setData(data: List<ProductResponse.Product>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}