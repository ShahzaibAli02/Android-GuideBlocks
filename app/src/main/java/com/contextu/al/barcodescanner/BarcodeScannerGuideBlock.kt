package com.contextu.al.barcodescanner

import android.app.Activity
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.contextu.al.model.customguide.GuidePayload
import com.google.gson.Gson

class BarcodeScannerGuideBlock(
    private val activity: ComponentActivity,
    onScanResult: (String?) -> Unit
) {
    private var guidePayload: GuidePayload? = null
    private val startForResult = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent == null) {
                onScanResult.invoke(null)
                guidePayload?.dismissGuide?.onClick(View(activity))
                return@registerForActivityResult
            }
            intent.getStringExtra(BarcodeScanningActivity.BARCODE_DATA)?.let {
                onScanResult.invoke(it)
                guidePayload?.clickInside?.onClick(View(activity))
            } ?: {
                onScanResult.invoke(null)
                guidePayload?.dismissGuide?.onClick(View(activity))
            }
        } else {
            onScanResult.invoke(null)
            guidePayload?.dismissGuide?.onClick(View(activity))
        }
    }

    fun showGuideBlock(guidePayload: GuidePayload) {
        this.guidePayload = guidePayload
        val property = Gson().toJson(guidePayload.guide)
        startForResult.launch(BarcodeScanningActivity.newIntent(activity, property))
    }
}