package com.contextu.al.common.model

data class TextModel(
    val fontSize: Int,
    val text: String,
    val fontWeight: String,
    val color: Int,
    val alignment: String = Alignments.center.name,
    val fontName: String? = null
)
