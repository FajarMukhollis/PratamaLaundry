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

        holder.view.findViewById<TextView>(R.id.id_product).text = result.id_product
        holder.view.findViewById<TextView>(R.id.name_product).text = result.nama_produk
        holder.view.findViewById<TextView>(R.id.service).text = result.jenis_service
        holder.view.findViewById<TextView>(R.id.harga).text = result.harga_produk
    }

    class ViewHolderProduct(val view: View): RecyclerView.ViewHolder(view)

    fun setData(data: List<ProductResponse.Product>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}