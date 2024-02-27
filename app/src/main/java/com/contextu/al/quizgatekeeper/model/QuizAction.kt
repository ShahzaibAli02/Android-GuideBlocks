package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class QuizAction(
    @SerializedName("allow_screen_access")
    var allowScreenAccess: Boolean? = null,
    @SerializedName("setTag")
    var setTag: SetTag? = null
)