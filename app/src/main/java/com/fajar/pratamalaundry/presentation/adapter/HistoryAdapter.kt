package com.fajar.pratamalaundry.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.model.response.HistoryResponse

class HistoryAdapter(private val results: ArrayList<HistoryResponse.Transaksi>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory>() {

    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var onReportClickListener: OnReportClickListener
    private lateinit var onPaymentClickListener: OnPaymentClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        return ViewHolderHistory(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_row_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {
        val result = results[position]
        holder.apply {
            no_pesanan.text = result.no_pesanan
            jenis_kategori.text = result.jenis_kategori
            nama_produk.text = result.nama_produk
            status_barang.text = result.status_barang
            total_harga.text = result.total_harga
            status_bayar.text = result.status_bayar
            berat.text = result.berat
            tgl_order.text = result.tgl_order
            tgl_selesai.text = result.tgl_selesai

            if (result.status_barang == "Selesai") {
                btn_report.visibility = View.VISIBLE
                btn_payment.visibility = View.GONE
            } else {
                btn_report.visibility = View.GONE
                btn_payment.visibility = View.VISIBLE
            }

            btn_report.setOnClickListener {
                onReportClickListener.onReportClick(position)
            }

            btn_payment.setOnClickListener {
                onPaymentClickListener.onPaymentClick(position)
            }
        }
    }

    inner class ViewHolderHistory(view: View) : RecyclerView.ViewHolder(view) {
        val no_pesanan = view.findViewById<TextView>(R.id.tv_noPesanan)
        val jenis_kategori = view.findViewById<TextView>(R.id.tv_kategori)
        val nama_produk = view.findViewById<TextView>(R.id.tv_product)
        val status_barang = view.findViewById<TextView>(R.id.tv_status)
        val total_harga = view.findViewById<TextView>(R.id.tv_price)
        val status_bayar = view.findViewById<TextView>(R.id.tv_status_bayar)
        val berat = view.findViewById<TextView>(R.id.tv_Berat)
        val tgl_order = view.findViewById<TextView>(R.id.tv_tgl_terima)
        val tgl_selesai = view.findViewById<TextView>(R.id.tv_tgl_selesai)
        val btn_report = view.findViewById<ImageView>(R.id.btnReport)
        val btn_payment = view.findViewById<ImageView>(R.id.btnPayment)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = results.size

    fun setData(data: List<HistoryResponse.Transaksi>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun setOnReportClickListener(listener: OnReportClickListener) {
        this.onReportClickListener = listener
    }

    fun setOnPaymentClickListener(listener: OnPaymentClickListener) {
        this.onPaymentClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnReportClickListener {
        fun onReportClick(position: Int)
    }

    interface OnPaymentClickListener {
        fun onPaymentClick(position: Int)
    }

    fun getItem(position: Int): HistoryResponse.Transaksi {
        return results[position]
    }
}