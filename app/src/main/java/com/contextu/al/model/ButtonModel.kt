package com.contextu.al.model

data class ButtonModel(
    val width: String = "0",
    val height: String = "0",
    val background: Int,
    val radius: Float = 0f,
    val border: Border = Border(0f, 0),
    val textModel: TextModel,
    val margin: Margin = Margin()
)