package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("correct")
    var correct: Boolean? = null,
    @SerializedName("label")
    var label: String? = null
)