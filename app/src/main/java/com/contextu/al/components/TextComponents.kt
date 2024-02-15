package com.contextu.al.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@Composable
fun AppTextView(
    size: Int,
    text: String,
    fontWeight: String?,
    color: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = size.toSP(),
        fontWeight = if (fontWeight?.contains("bold", ignoreCase = true) == true)
            FontWeight.Bold
        else FontWeight.Normal,
        color = Color(color),
        textAlign = textAlign,
        lineHeight = lineHeight
    )
}

@Composable
fun Int.toSP() = TextUnit(value = this.toFloat(), TextUnitType.Sp)