package com.contextu.al.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.contextu.al.model.ui.Box

fun Box.toBoxModifier(modifier: Modifier = Modifier): Modifier {
    val heightModifier = toHeightModifier(modifier = modifier)
    val widthModifier = toWidthModifier(modifier = heightModifier)
    val marginModifier = margin.toMarginModifier(widthModifier)
    val backGroundModifier = toBackGroundModifier(marginModifier)
    val cornerModifier = toCornerModifier(backGroundModifier)
    val borderModifier = toBorderModifier(cornerModifier)
    return padding.toPaddingModifier(borderModifier)
}

fun Box.toWidthModifier(modifier: Modifier = Modifier): Modifier {
    return if (width?.contains("%") == true) {
        val width = width?.split("%")?.firstOrNull()?.toIntOrNull()
        width?.let {
            modifier.fillMaxWidth((it / 100f))
        } ?: modifier
    } else {
        val width = width?.toFloatOrNull()
        width?.let {
            modifier.width(Dp(it))
        } ?: modifier
    }
}

fun Box.toHeightModifier(modifier: Modifier = Modifier): Modifier {
    return if (height?.contains("%") == true) {
        val height = height?.split("%")?.firstOrNull()?.toIntOrNull()
        height?.let {
            modifier.fillMaxHeight((it / 100f))
        } ?: modifier
    } else {
        val height = height?.toFloatOrNull()
        height?.let {
            modifier.height(Dp(it))
        } ?: modifier
    }
}

fun Box.BoxParams.toMarginModifier(modifier: Modifier = Modifier): Modifier {
    return modifier.padding(
        top = Dp(top.toFloat()),
        bottom = Dp(bottom.toFloat()),
        start = Dp(left.toFloat()),
        end = Dp(right.toFloat())
    )
}

fun Box.BoxParams.toPaddingModifier(modifier: Modifier = Modifier): Modifier {
    return modifier.padding(
        top = Dp(top.toFloat()),
        bottom = Dp(bottom.toFloat()),
        start = Dp(left.toFloat()),
        end = Dp(right.toFloat())
    )
}

fun Box.toBackGroundModifier(modifier: Modifier = Modifier): Modifier {
    return modifier.background(Color(backgroundColor))
}

fun Box.toCornerModifier(modifier: Modifier = Modifier): Modifier {
    return cornerRadius.toString().toFloatOrNull()?.let {
        modifier.clip(RoundedCornerShape(Dp(it)))
    } ?: modifier
}

fun Box.toBorderModifier(modifier: Modifier = Modifier): Modifier {
    return borderWidth.toString().toFloatOrNull()?.let {
        if (it > 0)
            modifier.border(Dp(it), Color(borderColor))
        else
            modifier
    } ?: modifier
}