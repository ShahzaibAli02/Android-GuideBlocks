package com.contextu.al.quizgatekeeper.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.contextu.al.mychecklist.models.Task
import com.contextu.al.quizgatekeeper.model.QuizGK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class QuizGateKeeperViewModel:ViewModel()
{

    private val _quizGk:MutableStateFlow<QuizGK?> = MutableStateFlow(null)
    val quizGK: StateFlow<QuizGK?> = _quizGk
    fun updateQuiz(quizGK: QuizGK)
    {
        _quizGk.value=quizGK
    }
}