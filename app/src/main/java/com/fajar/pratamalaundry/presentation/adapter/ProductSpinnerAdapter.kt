package com.fajar.pratamalaundry.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.fajar.pratamalaundry.model.response.ProductResponse

class ProductSpinnerAdapter(
    context: Context,
    private val products: List<ProductResponse.Product>
) : ArrayAdapter<ProductResponse.Product>(context, android.R.layout.simple_spinner_item, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val product = products[position]
        (view as TextView).text = "${product.nama_produk} - ${product.jenis_service} - ${product.harga_produk}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val product = products[position]
        (view as TextView).text = "${product.nama_produk} - ${product.jenis_service} - ${product.harga_produk}"
        return view
    }
}
