package com.contextu.al.mychecklist.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LazyDivider(index: Int, list: List<String>)
    {
        if (index < list.size - 1)
        {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), color = Color.Black.copy(alpha = 0.08f), thickness = 1.dp
            )
        }
    }