package com.contextu.al.barcodescanner

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import com.contextu.al.common.extensions.clickedInside
import com.contextu.al.common.extensions.clickedOutside
import com.contextu.al.common.extensions.dismiss
import com.contextu.al.common.model.BarCodeModel
import com.contextu.al.model.customguide.ContextualContainer
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

interface ResultListener {
    fun onResultReceived(resultCode:Int,intent: Intent?)
}
class BarcodeScannerGuideBlock(
    private val activity: ComponentActivity,
    private val onScanResult: (String?) -> Unit
) {



    companion object {
        const val BARCODE_TAG = "barCodeTag"
        var resultListener:ResultListener?=null
        var isShowing:Boolean=false
    }

    private var contextualContainer: ContextualContainer? = null
    private var barcodeTag: String = BARCODE_TAG


    fun showGuideBlock(contextualContainer: ContextualContainer) {
        if(isShowing)
        {
            Log.i(
                    "BarcodeScannerGuideBlock",
                    "Already showing"
            )
            return
        }
        Log.d(
                "BARCODESCANNER",
                "SHOW BAR SCANNER : "
        )
        resultListener=object : ResultListener{
            override fun onResultReceived(resultCode: Int, intent: Intent?)
            {

                Log.d(
                        "BARCODESCANNER",
                        "onResultReceived: ${resultCode}"
                )
                if (resultCode == Activity.RESULT_OK) {
                    if (intent == null) {
                        onScanResult.invoke(null)
                        contextualContainer.dismiss()
                        return
                    }
                    intent.getStringExtra(BarcodeScanningActivity.BARCODE_DATA)?.let {
                        onScanResult.invoke(it)
                        setResultAsTag(it)
                        Log.d(
                                "BARCODESCANNER",
                                "onResultReceived: ${it}"
                        )
                        contextualContainer.clickedInside()
                    } ?: {
                        onScanResult.invoke(null)
                        contextualContainer.dismiss()
                    }
                }
                else if(resultCode==Activity.RESULT_CANCELED)
                {
                    contextualContainer.clickedOutside()
                    onScanResult.invoke(null)
                }
                else {
                    onScanResult.invoke(null)
                    contextualContainer.dismiss()
                }

                isShowing=false
                resultListener=null

            }

        }
        this.contextualContainer = contextualContainer
        val property = Gson().toJson(contextualContainer.guidePayload.guide)
        val barCodeModel = Gson().fromJson(contextualContainer.guidePayload.guide.extraJson, BarCodeModel::class.java)
        barcodeTag = barCodeModel.properties.tag ?: BARCODE_TAG
        val intent=BarcodeScanningActivity.newIntent(activity, property)
        activity.startActivity(intent)
        isShowing=true
    }

    private fun setResultAsTag(result: String) {
        contextualContainer?.let {
            CoroutineScope(Dispatchers.IO).launch {
                it.tagManager.setStringTag(barcodeTag, result).collect()
            }
        }
    }
}