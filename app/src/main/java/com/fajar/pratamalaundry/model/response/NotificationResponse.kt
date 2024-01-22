package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@SerializedName("multicast_id")
	val multicastId: String,

	@SerializedName("success")
	val success: Int,

	@SerializedName("failure")
	val failure: Int,

	@SerializedName("canonical_ids")
	val canonicalIds: Int,

	@SerializedName("results")
    val results: ArrayList<ResultsItem>,

)

data class ResultsItem(

	@SerializedName("message_id")
    val messageId: String
)

