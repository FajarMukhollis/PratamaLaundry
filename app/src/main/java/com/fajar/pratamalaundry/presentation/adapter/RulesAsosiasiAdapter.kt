package com.fajar.pratamalaundry.presentation.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fajar.pratamalaundry.R
import com.fajar.pratamalaundry.model.response.RulesAsosiasiResponse

class RulesAsosiasiAdapter(
    private val results: ArrayList<RulesAsosiasiResponse.DataRulesAsosiasi>
) : RecyclerView.Adapter<RulesAsosiasiAdapter.ViewHolderRulesAsosiasi>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RulesAsosiasiAdapter.ViewHolderRulesAsosiasi {
        return ViewHolderRulesAsosiasi(
            View.inflate(parent.context, R.layout.item_row_rules, null)
        )
    }

    override fun onBindViewHolder(
        holder: RulesAsosiasiAdapter.ViewHolderRulesAsosiasi,
        position: Int
    ) {
        val result = results[position]
        holder.apply {
            id_rules.text = result.idRulesAsosiasi
            aturan.text = result.aturan
        }
    }

    override fun getItemCount(): Int = results.size

    inner class ViewHolderRulesAsosiasi(view: View) : RecyclerView.ViewHolder(view) {
        val id_rules = view.findViewById<TextView>(R.id.tv_id_rules)
        val aturan = view.findViewById<TextView>(R.id.tv_rules)
    }

    fun setData(data: List<RulesAsosiasiResponse.DataRulesAsosiasi>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

}