package com.contextu.al.quizgatekeeper.model


import com.google.gson.annotations.SerializedName

data class QuizGK(
    @SerializedName("fail")
    var fail: Fail? = null,
    @SerializedName("pass")
    var pass: Pass? = null,
    @SerializedName("Questions")
    var questions: List<Question?>? = null,


    var triesLeft:Int=0,
    var timeLeft:Int=0,
    var currentIndex:Int = 0,
    var isError:Boolean=false,
    var isLocked:Boolean=false,
    var errorMessage:String="",
)