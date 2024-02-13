package com.contextu.al.mychecklist

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.mychecklist.models.Task
import com.contextu.al.mychecklist.viewModels.TaskViewModel

class MyCheckListGuideBlocks : ComponentActivity()
{



//    override fun onCreate(savedInstanceState: Bundle?)
//    {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ComposeCheckListTheme { // A surface container using the 'background' color from the theme
//                Surface(modifier = androidx.compose.ui.Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")
//                }
//            }
//        }
//    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun show(contextualContainer: ContextualContainer)
    {


        val viewModel:TaskViewModel by viewModels()
        SideEffect {
            contextualContainer.guidePayload.guide.extraJson?.let {
                viewModel.parseJson(it)
            }

        }

        val taskListState = viewModel.list.collectAsState()

        // Now you can access the taskListState.value to get the current list value
        val taskList = taskListState.value
        val localDensity = LocalDensity.current

        val bottomSheetState = rememberBottomSheetScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        BottomSheetScaffold(
            sheetContent = {
                MyCheckList(taskList,modifier = Modifier.height((350-50).dp))
              },
            sheetPeekHeight = 350.dp,
            modifier = Modifier
                .systemBarsPadding()
        ) {
        }
        LaunchedEffect(bottomSheetState) {
            snapshotFlow { bottomSheetState.bottomSheetState.isVisible }.collect { isVisible ->
                if (isVisible) {

                } else {

                    //TODO HIDEN
                }
            }

        }


    }



    @Composable
    fun MyCheckList(taskList: List<Task>, modifier: Modifier)
    {

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

                        CheckListRow(
                            title=task.name,
                            enabled = task.enabled,
                            checked = task.checked,
                            onClick = {

                            },
                            onCheckChange = {

                            }

                        )
                        LazyDivider(index, list)
                    }
                }
            }


        }
    }

    @Composable
    private fun LazyDivider(index: Int, list: List<String>)
    {
        if (index < list.size - 1)
        {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), color = Color.Black.copy(alpha = 0.08f), thickness = 1.dp
            )
        }
    }


    @Composable
    fun CheckListRow(
        title:String,
        enabled:Boolean,
        checked:Boolean,
        onClick:()->Unit,
        onCheckChange:(checked:Boolean)->Unit,
        )
    {



        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .wrapContentHeight()
            .clickable {
                onClick()
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start){
            Text(
                text = title,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Checkbox(modifier = Modifier
                .size(15.dp)
                .padding(end = 10.dp),
                enabled = enabled,
                checked = checked, onCheckedChange = {
                    onCheckChange(it)
                }
            )

        }

    }

    @Preview(showBackground = true, heightDp = 500, widthDp = 300)
    @Composable
    fun GreetingPreview()
    {
        show(null)
    }
}