package com.fajar.pratamalaundry.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.model.response.RulesKomplainResponse

class RulesKomplainAdapter(
    private val results: ArrayList<RulesKomplainResponse.DataRulesKomplain>
) : RecyclerView.Adapter<RulesKomplainAdapter.ViewHolderRulesKomplain>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulesKomplainAdapter.ViewHolderRulesKomplain {
        return ViewHolderRulesKomplain(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_rules, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RulesKomplainAdapter.ViewHolderRulesKomplain, position: Int) {
        val result = results[position]
        holder.apply {
            id_rules.text = result.idRulesKomplain
            aturan.text = result.aturan
        }
    }

    override fun getItemCount(): Int = results.size

    inner class ViewHolderRulesKomplain(view: View) : RecyclerView.ViewHolder(view) {
        val id_rules = view.findViewById<TextView>(R.id.tv_id_rules)
        val aturan = view.findViewById<TextView>(R.id.tv_rules)
    }

    fun setData(data: List<RulesKomplainResponse.DataRulesKomplain>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

}