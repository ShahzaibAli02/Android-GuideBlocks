package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class Pass(
    @SerializedName("action")
    var action: String? = "",
    @SerializedName("action_data")
    var actionData: ActionDataX? = ActionDataX()
)