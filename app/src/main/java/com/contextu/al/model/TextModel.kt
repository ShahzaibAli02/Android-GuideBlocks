package com.contextu.al.model

data class TextModel(
    val fontSize: Int,
    val text: String,
    val fontWeight: String,
    val color: Int,
    val alignment: String = Alignment.center.name,
    val fontName: String? = null
)
