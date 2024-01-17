package com.fajar.pratamalaundry.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.fajar.pratamalaundry.model.response.GetCategoryResponse

class CategorySpinnerAdapter(
    context: Context,
    private val category: List<GetCategoryResponse.DataCategory>
) : ArrayAdapter<GetCategoryResponse.DataCategory>(
    context,
    android.R.layout.simple_spinner_item,
    category
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val category = category[position]
        (view as TextView).text = category.jenis_kategori
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val category = category[position]
        (view as TextView).text = category.jenis_kategori
        return view
    }

}