package com.contextu.al.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.contextu.al.model.customguide.GuidePayload

@Composable
fun ContainerComponent(
    modifier: Modifier = Modifier,
    guidePayload: GuidePayload,
    content: @Composable () -> Unit
) {

    with(guidePayload.guide.baseView) {

        val heightModifier = if (height?.contains("%") == true) {
            val height = height?.split("%")?.firstOrNull()?.toIntOrNull()
            height?.let {
                modifier.fillMaxHeight((it / 100f))
            } ?: modifier
        } else {
            val height = height?.toFloatOrNull()
            height?.let {
                Modifier.height(Dp(it))
            } ?: Modifier
        }

        val widthModifier = if (width?.contains("%") == true) {
            val height = width?.split("%")?.firstOrNull()?.toIntOrNull()
            height?.let {
                heightModifier.fillMaxWidth((it / 100f))
            } ?: heightModifier
        } else {
            val height = width?.toFloatOrNull()
            height?.let {
                heightModifier.width(Dp(it))
            } ?: heightModifier
        }

        Surface(
            modifier = widthModifier
                .padding(
                    top = Dp(margin.top.toFloat()),
                    bottom = Dp(margin.bottom.toFloat()),
                    start = Dp(margin.left.toFloat()),
                    end = Dp(margin.right.toFloat())
                )

                .clip(RoundedCornerShape(Dp(cornerRadius.toString().toFloatOrNull() ?: 0f))),
            color = Color(backgroundColor),
            border = BorderStroke(
                width = Dp(borderWidth.toString().toFloatOrNull() ?: 0f),
                color = Color(borderColor)
            )
        ) {
            Box(
                modifier = Modifier.padding(
                    top = Dp(padding.top.toFloat()),
                    bottom = Dp(padding.bottom.toFloat()),
                    start = Dp(padding.left.toFloat()),
                    end = Dp(padding.right.toFloat())
                )
            ) {
                content.invoke()
            }
        }
    }
}