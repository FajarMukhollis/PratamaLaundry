package com.fajar.pratamalaundry.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.fajar.pratamalaundry.model.response.ProductResponse
import com.fajar.pratamalaundry.model.response.ProdukByIdKategoriResponse

class ProductSpinnerAdapter(
    context: Context,
    private val products: List<ProdukByIdKategoriResponse.DataProduk>
) : ArrayAdapter<ProdukByIdKategoriResponse.DataProduk>(context, android.R.layout.simple_spinner_item, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val product = products[position]
        (view as TextView).text = "${product.namaProduk} - ${product.hargaProduk} - ${product.durasi}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val product = products[position]
        (view as TextView).text = "${product.namaProduk} - ${product.hargaProduk} - ${product.durasi}"
        return view
    }
}


