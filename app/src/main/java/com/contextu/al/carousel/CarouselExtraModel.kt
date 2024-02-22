package com.contextu.al.carousel

import com.google.gson.annotations.SerializedName

data class CarouselExtraModel(
    @SerializedName("guideBlockKey")
    val guideBlockKey: String,
    @SerializedName("skip")
    val skip: ActionProperties?,
) {
    data class ActionProperties (
        @SerializedName("text")
        val text: String,
        @SerializedName("color")
        val color: String,
        @SerializedName("size")
        val size: Int,
    )
}
