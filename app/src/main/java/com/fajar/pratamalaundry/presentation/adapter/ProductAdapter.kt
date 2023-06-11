package com.fajar.pratamalaundry.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.fajar.pratamalaundry.model.response.ProductResponse

class ProductAdapter(context: Context, resource: Int, private val productList: ArrayList<ProductResponse.Product>) :
    ArrayAdapter<ProductResponse.Product>(context, resource, productList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val productNameTextView = view.findViewById<TextView>(android.R.id.text1)
        val product = productList[position]
        productNameTextView.text = "${product.id_product}. ${product.nama_produk} - ${product.jenis_service} (Harga: ${product.harga_produk})"
        return view
    }
}