package com.contextu.al.mychecklist.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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