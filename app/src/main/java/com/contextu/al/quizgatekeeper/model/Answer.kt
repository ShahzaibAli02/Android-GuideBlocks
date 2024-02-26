package com.contextu.al.quizgatekeeper.model


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("row") var row: Row? = null,
)
{
    var isSelected: MutableState<Boolean> =  mutableStateOf(false)
}