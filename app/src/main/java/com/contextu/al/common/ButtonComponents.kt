package com.contextu.al.common

import androidx.compose.foundation.BorderStroke
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
import com.contextu.al.common.model.ButtonModel
import com.contextu.al.common.model.toBoxAlignment
import com.contextu.al.model.ui.Button
import com.contextu.al.model.ui.Text

@Composable
fun ButtonComponent(
    buttonModel: Button,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {

    if (buttonModel.text.isNullOrEmpty())
        return

    with(buttonModel) {

        Box(
            modifier = Modifier
                .padding(
                    top = Dp(margin.top.toFloat()),
                    start = Dp(buttonModel.margin.left.toFloat()),
                    end = Dp(
                        buttonModel.margin.right.toFloat()
                    ),
                    bottom = Dp(
                        buttonModel.margin.bottom.toFloat()
                    ),
                ),
        ) {

            val heightModifier = height?.toFloatOrNull()?.let {
                modifier.height(Dp(it))
            } ?: modifier

            val widthModifier = if (width?.contains("%") == true) {
                val height =
                    width?.split("%")?.firstOrNull()?.toIntOrNull()
                height?.let {
                    heightModifier.fillMaxWidth((it / 100f))
                } ?: heightModifier
            } else {
                val height = width?.toFloatOrNull()
                height?.let {
                    heightModifier.width(Dp(it))
                } ?: heightModifier
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = {
                        onButtonClick.invoke()
                    },
                    modifier = widthModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(backgroundColor)
                    ),
                    shape = RoundedCornerShape(Dp(cornerRadius.toString().toFloatOrNull() ?: 0f)),
                    border = BorderStroke(
                        Dp(borderWidth.toString().toFloatOrNull() ?: 0f),
                        Color(borderColor)
                    )
                ) {
                    val text = Text(
                        text = text,
                        color = color,
                        fontWeight = fontWeight,
                        fontSize = fontSize,
                        fontFamily = fontFamily
                    )
                    AppTextView(textProperties = text)
                }
            }
        }

    }
}

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
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
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
                        .fillMaxSize(),
                ) {
                    with(buttonModel.textModel) {
                        AppTextView(
                            size = fontSize,
                            text = text,
                            fontWeight = fontWeight,
                            color = color,
                            modifier = Modifier.align(alignment.toBoxAlignment())
                        )
                    }
                }
            }
        }
    }
}