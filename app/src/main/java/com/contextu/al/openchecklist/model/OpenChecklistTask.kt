package com.contextu.al.openchecklist.model

import com.contextu.al.clientsidetrigger.util.log.Log
import org.json.JSONException
import org.json.JSONObject

class OpenChecklistTask(
    val name: String,
    val action: OpenChecklistTaskAction,
    val actionData: OpenChecklistTaskActionData,
    var checked: Boolean
){
    var id: String = name.lowercase().trim().replace(" ","_")
    fun checkedKey(): String {
        return id + "_checked"
    }
}

data class OpenChecklistTaskActionData(
    val deepLink: String? = null,
    val key: String? = null,
    val value: String? = null
)

enum class OpenChecklistTaskAction{
    GoToScreen,
    SetTag,
    Undefined
}

fun parsJSONtoTaskList(json: String): List<OpenChecklistTask> {

    val list: MutableList<OpenChecklistTask> = mutableListOf()

    try {
        val jsonObject: JSONObject = JSONObject(json)
        val tasksArray = jsonObject.getJSONArray("tasks")

        for (i in 0 until tasksArray.length()) {
            val taskObject = tasksArray.getJSONObject(i)
            val taskActionDataObject = taskObject.getJSONObject("action_data")
            list.add(
                OpenChecklistTask(
                    name = taskObject.getString("name"),
                    action = when(taskObject.getString("action")){
                        "gotoScreen" -> OpenChecklistTaskAction.GoToScreen
                        "SetTag" -> OpenChecklistTaskAction.SetTag
                        else ->  OpenChecklistTaskAction.Undefined
                    },
                    actionData = OpenChecklistTaskActionData(
                        deepLink = taskActionDataObject.optString("deeplink"),
                        key = taskActionDataObject.optString("key"),
                        value = taskActionDataObject.optString("value"),
                    ),
                    checked = false
                )
            )
        }
    } catch (e: JSONException) {
        e.printStackTrace()
        Log.e("TaskViewModel","parseJson() Failed to parse json")
    }

    return list
}