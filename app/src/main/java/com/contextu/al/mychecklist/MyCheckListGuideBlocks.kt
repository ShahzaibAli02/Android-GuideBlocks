package com.contextu.al.mychecklist

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.mychecklist.composables.CheckListRow
import com.contextu.al.mychecklist.composables.LazyDivider
import com.contextu.al.mychecklist.models.ActionData
import com.contextu.al.mychecklist.models.Task
import com.contextu.al.mychecklist.models.TaskAction
import com.contextu.al.mychecklist.viewModels.TaskViewModel
import kotlin.math.log

class MyCheckListGuideBlocks : ComponentActivity()
{


    val LocalContextualContainer = staticCompositionLocalOf<ContextualContainer?> { null }

    @Composable
    fun show(contextualContainer: ContextualContainer)
    {

        
        CompositionLocalProvider(value = LocalContextualContainer provides contextualContainer ) {
            val viewModel:TaskViewModel by viewModels()
            SideEffect {
                contextualContainer.guidePayload.guide.extraJson?.let {
                    viewModel.parseJson(it)
                }
            }
            val taskListState = viewModel.list.collectAsState()
            val taskList = taskListState.value
            ShowBottomSheet(taskList)
        }


    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun ShowBottomSheet(taskList: List<Task>)
    {
        val bottomSheetState = rememberBottomSheetScaffoldState()
        BottomSheetScaffold(
            sheetContent = {
                MyCheckList(taskList, modifier = Modifier.height((350 - 50).dp))
            }, sheetPeekHeight = 350.dp, modifier = Modifier.systemBarsPadding()
        ) {}
        LaunchedEffect(bottomSheetState) {
            snapshotFlow { bottomSheetState.bottomSheetState.isVisible }.collect { isVisible ->
                Log.d("TAG", "ShowBottomSheet: visibility changed : ${isVisible}")
            }

        }
    }


    @Composable
    fun MyCheckList(taskList: List<Task>, modifier: Modifier)
    {

        val contextualContainer = LocalContextualContainer.current
        val list = List(20) { "" }

        Column(modifier = modifier){
            Text(
                text = "Check List title ",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)

            )
            Card (modifier = Modifier
                .padding(10.dp)
                .weight(1f)){
                LazyColumn(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxHeight()){
                    itemsIndexed(taskList) { index, task ->

                        var enabled by remember { mutableStateOf(false) } // Initial value doesn't matter
                        var checked by remember { mutableStateOf(false) } // Initial value doesn't matter

                        val coroutineScope = rememberCoroutineScope()

                        // Update enabled when the result of the suspending function is available
                        LaunchedEffect(index) {
                            enabled = task.getEnabled(contextualContainer!!)
                            checked = task.getChecked(contextualContainer!!)
                        }
                        Log.d("TAG", "MyCheckList: Enabled : ${enabled}")
                        CheckListRow(
                            title=task.name,
                            enabled = enabled,
                            checked = checked,
                            onClick = {
                            },
                            onCheckChange = {
                                if(it)
                                {
                                    Log.d("TAG","MyCheckList: Enabled : ${enabled}")
                                    task.setChecked(true,contextualContainer!!)
                                    task.doAction(contextualContainer!!)
                                }
                            }

                        )
                        LazyDivider(index, list)
                    }
                }
            }


        }
    }






    @Preview(showBackground = true, heightDp = 500, widthDp = 300)
    @Composable
    fun GreetingPreview()
    {
        val mLsit= List(5) {
            Task("", "Test ${it}", true, false, TaskAction.Unknown, ActionData(),)
        }
        ShowBottomSheet(mLsit)
    }
}