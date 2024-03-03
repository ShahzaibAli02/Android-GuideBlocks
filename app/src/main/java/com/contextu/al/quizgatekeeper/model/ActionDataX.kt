package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class ActionDataX(
    @SerializedName("allow_screen_access")
    var allowScreenAccess: Boolean? = null,
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("value")
    var value: String? = null
)