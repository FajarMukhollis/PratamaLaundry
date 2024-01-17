package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class RulesKomplainResponse(
	@SerializedName("status")
	val status: Boolean,

	@SerializedName("message")
	val message: String,

	@SerializedName("data")
	val data: ArrayList<DataRulesKomplain>,
) {
	data class DataRulesKomplain(
		@SerializedName("id_rules_komplain")
		val idRulesKomplain: String,

		@SerializedName("id_pelanggan")
		val idPetugas: String,

		@SerializedName("aturan")
		val aturan: String

	)
}

