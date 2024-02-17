package com.contextu.al.model

import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

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

enum class Alignments(val alignment: String) {
    leftTop("left-top"),
    center("center"),
    left("left"),
    right("right"),
    topCenter("center-top")
}

fun String.toBoxAlignment(): Alignment {
    return when {
        this == Alignments.center.alignment -> {
            Alignment.Center
        }
        this == Alignments.left.alignment -> {
            Alignment.CenterStart
        }
        this == Alignments.right.alignment -> {
            Alignment.CenterEnd
        }
        this == Alignments.leftTop.alignment -> {
            Alignment.TopStart
        }
        this == Alignments.topCenter.alignment -> {
            Alignment.TopCenter
        }
        else -> {
            Alignment.Center
        }
    }
}

fun String.toTextAlign(): TextAlign {
    return when {
        this == Alignments.center.alignment -> {
            TextAlign.Center
        }
        this == Alignments.left.alignment -> {
            TextAlign.Start
        }
        this == Alignments.right.alignment -> {
            TextAlign.End
        }
        else -> {
            TextAlign.Center
        }
    }
}