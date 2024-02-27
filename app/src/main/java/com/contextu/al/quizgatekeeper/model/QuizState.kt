package com.contextu.al.quizgatekeeper.model

import com.contextu.al.quizgatekeeper.enums.QuizStatus

data class QuizState(var time:Long,var quizStatus:QuizStatus,var retriesLeft:Int)
