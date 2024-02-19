package com.contextu.al.common.model

import android.graphics.Color
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarCodeModel(
    @SerializedName("properties")
    val properties: Properties
): Parcelable {
    @Parcelize
    data class Properties(
        @SerializedName("width")
        val width: Float? = 70f,
        @SerializedName("height")
        val height: Float? = 35f,
        @SerializedName("show_qr_result")
        val showResult: Boolean = false,
        @SerializedName("iconProperties")
        val iconProperties: IconProperties = IconProperties(),
        @SerializedName("tag")
        val tag: String? = null
    ): Parcelable

    @Parcelize
    data class IconProperties(
        @SerializedName("color")
        val color: Int = Color.WHITE,
        @SerializedName("size")
        val size: Float? = 30f
    ): Parcelable
}
