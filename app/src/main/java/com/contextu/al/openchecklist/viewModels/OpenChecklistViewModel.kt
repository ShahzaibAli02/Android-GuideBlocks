package com.contextu.al.openchecklist.viewModels

import androidx.lifecycle.ViewModel
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.openchecklist.model.OpenChecklistTask
import com.contextu.al.openchecklist.model.parsJSONtoTaskList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OpenChecklistViewModel : ViewModel(){

    private val _title = MutableStateFlow<String>("")
    var title = _title.asStateFlow()

    private val _tasks = MutableStateFlow<List<OpenChecklistTask>>(emptyList())
    var tasks = _tasks.asStateFlow()

    private val _contextualContainer = MutableStateFlow<ContextualContainer?>(null)
    var contextualContainer = _contextualContainer.asStateFlow()

    fun updateData(contextualContainer: ContextualContainer){
        _title.value = contextualContainer.guidePayload.guide.titleText.text ?: ""
        _tasks.value = parsJSONtoTaskList(contextualContainer.guidePayload.guide.extraJson ?: "")

        _contextualContainer.value = contextualContainer

        _tasks.value.forEach {
            CoroutineScope(Dispatchers.IO).launch {
                contextualContainer.tagManager.getTag(it.checkedKey()).collectLatest { tags ->
                    val checked = tags?.tagStringValue ?: ""
                    it.checked = checked == "true"
                }
            }
        }
    }

    fun setTaskAsChecked(task: OpenChecklistTask){
        _contextualContainer.value?.tagManager?.setStringTag(task.checkedKey(),"true")
    }

    fun setTag(key:String?, value:String?){
        if(key != null && value != null){
            _contextualContainer.value?.tagManager?.setStringTag(key,value)
        }
    }



}