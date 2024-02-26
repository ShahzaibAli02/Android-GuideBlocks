package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class Pass(
    @SerializedName("quiz_action")
    var quizAction: QuizAction? = null
)