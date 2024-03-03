package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class Fail(
    @SerializedName("action")
    var action: String? = null,
    @SerializedName("action_data")
    var actionData: ActionData? = null
)