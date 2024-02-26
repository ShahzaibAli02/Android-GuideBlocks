package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class Fail(
    @SerializedName("allow_screen_access")
    var allowScreenAccess: Boolean? = null,
    @SerializedName("lockout_seconds")
    var lockoutSeconds: Int? = null,
    @SerializedName("quiz_action")
    var quizAction: String? = null,
    @SerializedName("retries")
    var retries: Int? = null
)