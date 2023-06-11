package com.fajar.pratamalaundry.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.model.response.HistoryResponse
import com.fajar.pratamalaundry.model.response.ProductResponse

class HistoryAdapter(private val results: ArrayList<HistoryResponse.Transaksi>)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        LayoutInflater.from(parent.context).inflate(R.layout.item_row_history, parent, false).apply {
            return ViewHolderHistory(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {
        val result = results[position]
        holder.view.findViewById<TextView>(R.id.tv_product).text = result.nama_produk
        holder.view.findViewById<TextView>(R.id.tv_status).text = result.status_barang
        holder.view.findViewById<TextView>(R.id.tv_price).text = result.total_harga
        holder.view.findViewById<TextView>(R.id.tv_status_bayar).text = result.status_bayar
        holder.view.findViewById<TextView>(R.id.tv_Berat).text = result.berat
        holder.view.findViewById<TextView>(R.id.tv_tgl_terima).text = result.tgl_order
        holder.view.findViewById<TextView>(R.id.tv_tgl_selesai).text = result.tgl_selesai
    }

    class ViewHolderHistory (val view: View): RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = results.size

    fun setData(data: List<HistoryResponse.Transaksi>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}