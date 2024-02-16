package com.contextu.al.mychecklist.models

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.contextu.al.model.customguide.ContextualContainer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull

 class Task
{

    var id:String?=null
    val name:String?=null
    var checked:Boolean?=false
    var enabled:Boolean?=true
    val action:TaskAction?=null

     val action_data:ActionData?=null
    suspend fun getChecked(contextualContainer:ContextualContainer):Boolean
    {

        if(checked==false)
        {
            getCheckedKey().let {

                return contextualContainer.tagManager.getTag(it).firstOrNull()?.tagStringValue.equals("true",true)

            }
        }
        else
        {
            return true
        }


    }

    fun setChecked(checked: Boolean,contextualContainer:ContextualContainer)
    {

        this.checked=checked
        getCheckedKey()?.let {
            contextualContainer.tagManager.setStringTag(it, checked.toString())
        }


    }

    suspend fun getEnabled(contextualContainer:ContextualContainer):Boolean
    {
        return getChecked(contextualContainer).not()
    }

    fun doAction(deepLinkListener:(String)->Unit,contextualContainer:ContextualContainer)
    {
        when(action)
        {
            TaskAction.SetTag -> {
                        action_data?.key?.let {key->
                            contextualContainer.tagManager.setStringTag(key,action_data.value?:"")
                        }


                Log.d("Task", "doAction: SetTag Done")
            }
            TaskAction.gotoScreen -> {
                action_data?.deeplink?.let{
                    deepLinkListener(it)
                }
            }
            TaskAction.Unknown -> {
                Log.d("Task", "doAction: Unknown")
            }
            else ->
            {

            }
        }

    }


    private fun getCheckedKey():String
    {
        id = name?.lowercase()?.trim()?.replace(" ", "_")?:""
        return  id + "_checked"
    }
}
