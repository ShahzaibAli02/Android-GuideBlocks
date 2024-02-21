package com.contextu.al.barcodescanner

import android.app.Activity
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.contextu.al.common.model.BarCodeModel
import com.contextu.al.model.customguide.ContextualContainer
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BarcodeScannerGuideBlock(
    private val activity: ComponentActivity,
    onScanResult: (String?) -> Unit
) {

    companion object {
        const val BARCODE_TAG = "barCodeTag"
    }

    private var contextualContainer: ContextualContainer? = null
    private var barcodeTag: String = BARCODE_TAG

    private val startForResult =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent == null) {
                    onScanResult.invoke(null)
                    contextualContainer?.guidePayload?.dismissGuide?.onClick(View(activity))
                    return@registerForActivityResult
                }
                intent.getStringExtra(BarcodeScanningActivity.BARCODE_DATA)?.let {
                    onScanResult.invoke(it)
                    setResultAsTag(it)
                    contextualContainer?.guidePayload?.clickInside?.onClick(View(activity))
                } ?: {
                    onScanResult.invoke(null)
                    contextualContainer?.guidePayload?.dismissGuide?.onClick(View(activity))
                }
            } else {
                onScanResult.invoke(null)
                contextualContainer?.guidePayload?.dismissGuide?.onClick(View(activity))
            }
        }

    fun showGuideBlock(contextualContainer: ContextualContainer) {
        this.contextualContainer = contextualContainer
        val property = Gson().toJson(contextualContainer.guidePayload.guide)

        val barCodeModel = Gson().fromJson(contextualContainer.guidePayload.guide.extraJson, BarCodeModel::class.java)
        barcodeTag = barCodeModel.properties.tag ?: BARCODE_TAG
        startForResult.launch(BarcodeScanningActivity.newIntent(activity, property))
    }

    private fun setResultAsTag(result: String) {
        contextualContainer?.let {
            CoroutineScope(Dispatchers.IO).launch {
                it.tagManager.setStringTag(barcodeTag, result).collect()
            }
        }
    }
}