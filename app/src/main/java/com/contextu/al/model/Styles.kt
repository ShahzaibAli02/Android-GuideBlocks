package com.contextu.al.model

data class Margin(
    val top: Int = 0,
    val bottom: Int = 0,
    val left: Int = 0,
    val right: Int = 0
)

data class Border(
    val width: Float = 0f,
    val color: Int = 0
)

enum class Alignment {
    center,
    left,
    right
}