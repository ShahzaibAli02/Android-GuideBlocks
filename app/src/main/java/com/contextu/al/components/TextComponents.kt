package com.contextu.al.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.contextu.al.model.toBoxAlignment
import com.contextu.al.model.toTextAlign

@Composable
fun AppTextView(
    textProperties: com.contextu.al.model.ui.Text,
    modifier: Modifier = Modifier
) {
    with(textProperties) {
        Box(
            modifier = Modifier.padding(
                top = Dp(margin.top.toFloat()),
                bottom = Dp(margin.bottom.toFloat()),
                start = Dp(margin.left.toFloat()),
                end = Dp(margin.right.toFloat())
            )
                .background(Color(backgroundColor))
                .padding(
                    top = Dp(padding.top.toFloat()),
                    bottom = Dp(padding.bottom.toFloat()),
                    start = Dp(padding.left.toFloat()),
                    end = Dp(padding.right.toFloat())
                )
        ) {
            Text(
                text = text ?: "",
                modifier = modifier
                    .fillMaxWidth()
                    .align(textAlign.toBoxAlignment()),
                fontSize = fontSize.toSP(),
                fontWeight = if (fontWeight?.contains("bold", ignoreCase = true) == true)
                    FontWeight.Bold
                else FontWeight.Normal,
                color = Color(color),
                textAlign = textAlign.toTextAlign(),
                lineHeight = (fontSize + 10).toSP()
            )
        }
    }
}

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