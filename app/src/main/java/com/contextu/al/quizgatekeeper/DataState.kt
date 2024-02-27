package com.contextu.al.quizgatekeeper

sealed class DataState<T>
{

    class Success<T>(val data:T) : DataState<T>()
    class Error<T>(val errorMsg:String):DataState<T>()

}