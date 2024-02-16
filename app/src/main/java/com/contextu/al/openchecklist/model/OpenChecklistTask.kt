package com.contextu.al.openchecklist.model

import org.json.JSONException
import org.json.JSONObject

class OpenChecklistTask(
    val name: String,
    val action: String,
    val actionData: OpenChecklistTaskActionData,
    val checked: Boolean
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
                    action = taskObject.getString("action"),
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
    }

    return list
}