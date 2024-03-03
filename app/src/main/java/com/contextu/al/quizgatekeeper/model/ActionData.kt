package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class ActionData(
    @SerializedName("allow_screen_access")
    var allowScreenAccess: Boolean? = null,
    @SerializedName("attempts")
    var attempts: Int? = null,
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("lockout_seconds")
    var lockoutSeconds: Int? = null,
    @SerializedName("value")
    var value: String? = null
)