package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class QuizGK(
    @SerializedName("fail")
    var fail: Fail? = Fail(),
    @SerializedName("guideBlockKey")
    var guideBlockKey: String? = "",
    @SerializedName("pass")
    var pass: Pass? = Pass(),
    @SerializedName("questions")
    var questions: List<Question>? = listOf()
)