package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class QuizGK(
    @SerializedName("fail")
    var fail: Fail? = null,
    @SerializedName("pass")
    var pass: Pass? = null,
    @SerializedName("Questions")
    var questions: List<Question?>? = null,

)