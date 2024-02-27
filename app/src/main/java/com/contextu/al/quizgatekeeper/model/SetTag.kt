package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class SetTag(
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("value")
    var value: String? = null
)