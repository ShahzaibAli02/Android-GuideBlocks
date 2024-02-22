package com.contextu.al.barcodescanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BarcodeField(val label: String, val value: String) : Parcelable
