package com.contextu.al.mychecklist.viewModels

import androidx.lifecycle.ViewModel
import com.contextu.al.clientsidetrigger.util.log.Log
import com.google.gson.Gson
import com.contextu.al.mychecklist.models.Task
import com.contextu.al.mychecklist.models.TaskList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel:ViewModel()
{


    private val _list: MutableStateFlow<List<Task>> = MutableStateFlow(emptyList())
    val list: StateFlow<List<Task>> = _list

    fun updateList(newList: List<Task>) {
        _list.value = newList
    }

    fun parseJson(json:String)
    {
        val gson = Gson()
        val taskList = gson.fromJson(json, TaskList::class.java)
        taskList?.tasks?.let {
            updateList(it) }?: Log.e("TaskViewModel","parseJson() Failed to parse json")
    }
    

}