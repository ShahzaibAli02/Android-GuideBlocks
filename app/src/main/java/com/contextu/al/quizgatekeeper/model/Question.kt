package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("answers")
    var answers: List<Answer?>? = null,
    @SerializedName("question")
    var question: String? = null
)