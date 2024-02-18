package com.contextu.al.common.model

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
        @SerializedName("title")
        val title: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("width")
        val width: Float? = 70f,
        @SerializedName("height")
        val height: Float? = 35f,
    ): Parcelable
}
