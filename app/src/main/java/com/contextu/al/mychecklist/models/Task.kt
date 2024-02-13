package com.contextu.al.mychecklist.models

import android.util.Log
import com.contextu.al.model.customguide.ContextualContainer
import kotlinx.coroutines.flow.firstOrNull

data class Task(var id:String="",val name:String,var checked:Boolean=false,var enabled:Boolean=true,val action:TaskAction=TaskAction.Unknown,val action_data:ActionData)
{


    var checkedKey: String=""
    suspend fun getChecked(contextualContainer:ContextualContainer):Boolean
    {
        return contextualContainer.tagManager.getTag(checkedKey).firstOrNull()?.tagStringValue.equals("true",true)
    }

    fun setChecked(checked: Boolean,contextualContainer:ContextualContainer)
    {

        this.checked=checked
        contextualContainer.tagManager.setStringTag(checkedKey, checked.toString())


//        action_data.key?.let {key->
//            contextualContainer.tagManager.setStringTag(key,action_data.value?:"")
//        }

    }

    fun doAction(contextualContainer:ContextualContainer)
    {
        when(action)
        {
            TaskAction.SetTag -> {
                        action_data.key?.let {key->
                            contextualContainer.tagManager.setStringTag(key,action_data.value?:"")
                        }
                Log.d("Task", "doAction: SetTag Done")
            }
            TaskAction.gotoScreen -> {
                //TODO GO TO SCREEN
            }
            TaskAction.Unknown -> {
                Log.d("Task", "doAction: Unknown")
            }
        }

    }
    init
    {
        id=name.lowercase()
            .trim()
            .replace(" ", "_")
        checkedKey=id + "_checked"
    }
}
