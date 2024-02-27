package com.contextu.al.quizgatekeeper.viewModel

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.contextu.al.mychecklist.models.Task
import com.contextu.al.quizgatekeeper.enums.QuizStatus
import com.contextu.al.quizgatekeeper.model.QuizGK
import com.contextu.al.quizgatekeeper.model.QuizState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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


    fun updateQuiz(quizGK: QuizGK)
    {
        _quizGk.value=quizGK
        updateState{
            it.apply {
                retriesLeft=quizGK.fail?.retries?:0
                time=quizGK.fail?.lockoutSeconds?.toLong()?:0L
                quizStatus=QuizStatus.STARTED
            }
        }
    }
    fun updateState(quizState: (QuizState)->QuizState)
    {
        quizState(_quizState.value).apply {
            Log.d("TAG", "updateState: ${this?.quizStatus}")
            _quizState.value=this.copy()
        }
    }

    private var timer: CountDownTimer? = null
//    fun startTimer() {
//        timer?.cancel()
//        timer = object : CountDownTimer( _quizState.value.time, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                updateState(){quizState->
//                    if(quizState.quizStatus==QuizStatus.STARTED)
//                    {
//                        quizState.apply {
//                            time=millisUntilFinished
//                        }
//                    }
//                    else
//                    {
//                        cancel()
//                        null
//                    }
//                }
//            }
//
//            override fun onFinish() {
//                updateState(){quizState->
//                    if(quizState.quizStatus==QuizStatus.STARTED)
//                    {
//                        quizState.apply {
//                            quizStatus=QuizStatus.TIMEUP
//                        }
//                    }
//                    else null
//
//                }
//            }
//        }
//        timer?.start()
//    }
}