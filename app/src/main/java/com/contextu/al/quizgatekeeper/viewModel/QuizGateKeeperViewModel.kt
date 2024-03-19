package com.contextu.al.quizgatekeeper.viewModel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import com.contextu.al.quizgatekeeper.enums.QuizStatus
import com.contextu.al.quizgatekeeper.model.QuizGK
import com.contextu.al.quizgatekeeper.model.QuizState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QuizGateKeeperViewModel:ViewModel()
{

    private val _quizGk:MutableStateFlow<QuizGK?> = MutableStateFlow(null)
    private val _quizState:MutableStateFlow<QuizState> = MutableStateFlow(QuizState(0,QuizStatus.NONE,0))



    val quizState:StateFlow<QuizState> = _quizState
    val quizGK: StateFlow<QuizGK?> = _quizGk

    var quizQuestionsSize=0
        get()
        {
            field=_quizGk.value?.questions?.size?:0
            return field
        }

    var quizAttemptsLimit=0
        get()
        {
            field=_quizGk.value?.fail?.actionData?.attempts?:0
            return field
        }


    fun updateQuiz(quizGK: QuizGK,state:QuizStatus)
    {
        _quizGk.value=quizGK
        updateState{
            it.apply {
                retriesLeft=quizGK.fail?.actionData?.attempts?:0
                time=quizGK.fail?.actionData?.lockoutSeconds?.toLong()?:0L
                quizStatus=state
            }
        }
    }
    fun updateState(quizState: (QuizState)->QuizState)
    {
        quizState(_quizState.value.copy()).let {newQuizState->
            _quizState.value=newQuizState
        }
    }

}
