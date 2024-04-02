package com.contextu.al.mychecklist

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contextu.al.common.extensions.clickedInside
import com.contextu.al.common.extensions.clickedOutside
import com.contextu.al.common.extensions.complete
import com.contextu.al.common.extensions.dismiss
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.mychecklist.composables.CheckListRow
import com.contextu.al.mychecklist.composables.LazyDivider
import com.contextu.al.mychecklist.models.Task
import com.contextu.al.mychecklist.viewModels.TaskViewModel
import com.contextu.al.common.extensions.getIntegerTag
import com.contextu.al.common.extensions.getStringTag
import com.contextu.al.common.extensions.setTag
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

class MyCheckListGuideBlocks
{


    private val MY_CHECK_LIST_DONE="is_my_check_list_done"
    val LocalContextualContainer = staticCompositionLocalOf<ContextualContainer?> { null }
    var deepLinkListener: ((String) -> Unit)? = null
    @Composable
    fun show(
        deepLinkListener: ((String) -> Unit),
        activity: AppCompatActivity,
        contextualContainer: ContextualContainer,
    )
    {

        this.deepLinkListener = deepLinkListener
        var shouldShow by remember { mutableStateOf(true) }
        CompositionLocalProvider(LocalContextualContainer provides contextualContainer) {
            val viewModel: TaskViewModel by activity.viewModels()
            LaunchedEffect(key1 = contextualContainer) {
                contextualContainer.guidePayload.guide.extraJson?.let {
                    viewModel.parseJson(it)
                }

                if(contextualContainer.getStringTag("extraJson","") ==contextualContainer.guidePayload.guide.extraJson && contextualContainer.getIntegerTag(MY_CHECK_LIST_DONE)==1)
                {
                    shouldShow=false
                    return@LaunchedEffect
                }
            }
            val taskListState = viewModel.list.collectAsState()
            val taskList = taskListState.value

            if(shouldShow)
            {
                ShowBottomSheet(
                    contextualContainer.guidePayload.guide.titleText.text ?: "N/A", taskList
                )
            }

        }

    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun ShowBottomSheet(title: String, taskList: List<Task>)
    {
        var isClosedManually = remember {false}
        val contextualContainer=LocalContextualContainer.current
        val bottomSheetState = rememberBottomSheetScaffoldState(bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false))
        val coroutineScope = rememberCoroutineScope()
        BottomSheetScaffold(
            scaffoldState = bottomSheetState, sheetContent = {
                Row(modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        contextualContainer?.clickedInside()
                    }) {
                    IconButton(modifier = Modifier.size(20.dp), onClick = {
                        coroutineScope.launch {
                            isClosedManually=true
                            bottomSheetState.bottomSheetState.hide()
                            if(taskList.all { it.getChecked(contextualContainer!!) })
                            {
                                contextualContainer?.complete()
                                contextualContainer?.setTag("extraJson", contextualContainer.guidePayload.guide.extraJson ?:"")
                                contextualContainer?.setTag(MY_CHECK_LIST_DONE,1)
                            }
                            else
                                contextualContainer?.dismiss()
                        }
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                }


                MyCheckList(title, taskList, modifier = Modifier.height((350 - 50).dp))
            }, sheetPeekHeight = 350.dp, modifier = Modifier.systemBarsPadding()
        ) {

            LaunchedEffect(Random.nextInt()) {
                bottomSheetState.bottomSheetState.expand()
                snapshotFlow { bottomSheetState.bottomSheetState.isVisible }.collectLatest {isVisible->
                    if(!isClosedManually && !isVisible)
                    {
                        contextualContainer?.clickedOutside()
                    }
                }
            }
        }


    }


    @Composable
    fun MyCheckList(title: String, taskList: List<Task>, modifier: Modifier)
    {

        val contextualContainer = LocalContextualContainer.current
        Column(modifier = modifier) {
            Text(
                text = title, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)

            )
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxHeight()
                ) {
                    itemsIndexed(taskList) { index, task ->

                        var enabled by remember { mutableStateOf(false) }
                        var checked by remember { mutableStateOf(false) }
                        LaunchedEffect(Random.nextInt()) {
                            contextualContainer?.let {
                                enabled = task.getEnabled(contextualContainer)
                                checked = task.getChecked(contextualContainer)
                            }

                        }
                        CheckListRow(title = task.name
                            ?: "", enabled = enabled, checked = checked, onClick = {}, onCheckChange = { //                                enabled=it
                            //                                checked=it
                            if (it)
                            {
                                contextualContainer?.let {
                                    task.setChecked(true, contextualContainer)
                                    task.doAction(contextualContainer = contextualContainer, deepLinkListener = {
                                        deepLinkListener?.invoke(it)
                                    })


                                    enabled = false
                                    checked = true
                                }

                            }
                        }

                        )
                        LazyDivider(index, taskList.size)
                    }
                }
            }


        }
    }

}