package com.contextu.al.openchecklist.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _tasks = MutableStateFlow<ArrayList<OpenChecklistTask>>(ArrayList())
    var tasks = _tasks.asStateFlow()

    private val _contextualContainer = MutableStateFlow<ContextualContainer?>(null)

    init{
        viewModelScope.launch(Dispatchers.IO) {
            tasks.collect {
                it.forEachIndexed { index, task ->
                    CoroutineScope(Dispatchers.IO).launch {
                        _contextualContainer.value?.let { container ->
                            container.tagManager.getTag("checklist_row_$index").collectLatest { tags ->
                                val checked = tags?.tagStringValue ?: ""
                                _tasks.value[index].checked = checked == "checked"
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateData(contextualContainer: ContextualContainer){
        _title.value = contextualContainer.guidePayload.guide.titleText.text ?: ""
        _tasks.value = parsJSONtoTaskList(contextualContainer.guidePayload.guide.extraJson ?: "")

        _contextualContainer.value = contextualContainer

        _tasks.value.forEachIndexed { index, task ->
            CoroutineScope(Dispatchers.IO).launch {
                _contextualContainer.value?.let { container ->
                    container.tagManager.getTag("checklist_row_$index").collectLatest { tags ->
                        val checked = tags?.tagStringValue ?: ""
                        _tasks.value[index].checked = checked == "checked"
                    }
                }
            }
        }
    }

    fun setTaskAsChecked(index: Int){
        CoroutineScope(Dispatchers.IO).launch {
            _contextualContainer.value?.tagManager?.setStringTag("checklist_row_$index", "checked")?.collectLatest {
                updateData(_contextualContainer.value!!)
            }
        }
    }

    fun setTag(key:String?, value:String?){
        if(key != null && value != null){
            _contextualContainer.value?.tagManager?.setStringTag(key,value)
        }
    }



}