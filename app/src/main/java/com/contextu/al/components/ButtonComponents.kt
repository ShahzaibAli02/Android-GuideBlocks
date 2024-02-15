package com.contextu.al.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.contextu.al.model.ButtonModel

@Composable
fun ButtonComponent(
    buttonModel: ButtonModel,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {

    if (buttonModel.textModel.text.isEmpty())
        return

    Box(
        modifier = Modifier
            .padding(
                start = Dp(buttonModel.margin.left.toFloat()),
                end = Dp(
                    buttonModel.margin.right.toFloat()
                ),
                bottom = Dp(
                    buttonModel.margin.bottom.toFloat()
                ),
            ),
    ) {

        val heightModifier = buttonModel.height.toFloatOrNull()?.let {
            modifier.height(Dp(it))
        } ?: modifier

        val widthModifier = if (buttonModel.width.contains("%")) {
            val height =
                buttonModel.width.split("%").firstOrNull()?.toIntOrNull()
            height?.let {
                heightModifier.fillMaxWidth((it / 100f))
            } ?: heightModifier
        } else {
            val height = buttonModel.width.toFloatOrNull()
            height?.let {
                heightModifier.width(Dp(it))
            } ?: heightModifier
        }

        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Spacer(
                modifier = modifier.height(
                    Dp(buttonModel.margin.top.toFloat())
                )
            )

            Button(
                onClick = {
                    onButtonClick.invoke()
                },
                modifier = widthModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(buttonModel.background)
                ),
                border = BorderStroke(
                    Dp(buttonModel.border.width),
                    Color(buttonModel.border.color)
                ),
                shape = RoundedCornerShape(Dp(buttonModel.radius))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                ) {
                    with(buttonModel.textModel) {
                        AppTextView(
                            size = fontSize,
                            text = text,
                            fontWeight = fontWeight,
                            color = color,
                            modifier = Modifier.align(
                                when (alignment) {
                                    com.contextu.al.model.Alignment.center.name -> Alignment.Center
                                    com.contextu.al.model.Alignment.left.name -> Alignment.CenterStart
                                    com.contextu.al.model.Alignment.right.name -> Alignment.CenterEnd
                                    else -> Alignment.Center
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}