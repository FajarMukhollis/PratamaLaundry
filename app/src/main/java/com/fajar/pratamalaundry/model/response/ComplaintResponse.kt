package com.fajar.pratamalaundry.model.response

import com.google.gson.annotations.SerializedName

data class ComplaintResponse(
    @SerializedName("status")
    var status : Boolean,

    @SerializedName("message")
    var message : String
)
