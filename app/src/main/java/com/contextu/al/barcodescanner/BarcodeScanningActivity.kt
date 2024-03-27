package com.contextu.al.barcodescanner

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.contextu.al.Contextual
import com.contextu.al.ContextualBase
import com.contextu.al.R
import com.contextu.al.barcodescanner.camera.CameraSource
import com.contextu.al.barcodescanner.camera.CameraSourcePreview
import com.contextu.al.barcodescanner.camera.GraphicOverlay
import com.contextu.al.barcodescanner.camera.WorkflowModel
import com.contextu.al.barcodescanner.camera.WorkflowModel.*
import com.contextu.al.common.AppTextView
import com.contextu.al.common.ContainerComponent
import com.contextu.al.common.model.BarCodeModel
import com.contextu.al.model.customguide.GuidePayload
import com.contextu.al.common.utils.CameraUtils
import com.google.android.gms.common.internal.Objects
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import java.io.IOException
import java.util.ArrayList

class BarcodeScanningActivity : AppCompatActivity() {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var promptChip: Chip? = null
    private var promptChipAnimator: AnimatorSet? = null
    private var workflowModel: WorkflowModel? = null
    private var currentWorkflowState: WorkflowState? = null
    private var scannerWidth = 70f
    private var scannerHeight = 80f
    private var barCodeModel: BarCodeModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_barcode)
        preview = findViewById(R.id.camera_preview)
        graphicOverlay = findViewById<GraphicOverlay>(R.id.camera_preview_graphic_overlay).apply {
            cameraSource = CameraSource(this)
        }

        promptChip = findViewById(R.id.bottom_prompt_chip)
        promptChipAnimator =
            (AnimatorInflater.loadAnimator(
                this,
                R.animator.bottom_prompt_chip_enter
            ) as AnimatorSet).apply {
                setTarget(promptChip)
            }

        intent.extras?.getString(PROPERTY)?.let {
            Gson().fromJson(it, ContextualBase::class.java)?.let {
                setComposeView(it)
            }
        }

        setUpWorkflowModel()
    }

    private fun setComposeView(contextualBase: ContextualBase) {
        barCodeModel =
            Gson().fromJson(contextualBase.extraJson, BarCodeModel::class.java)
                ?: BarCodeModel(
                    properties = BarCodeModel.Properties(
                        width = scannerWidth,
                        height = scannerHeight
                    )
                )
        findViewById<ComposeView>(R.id.scannerComposeView).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val flashStatus = remember {
                    mutableStateOf(false)
                }
                MaterialTheme {
                    ContainerComponent(guidePayload = GuidePayload(
                        guide = contextualBase,
                        {}, {}, {}, {}, {}, {}
                    )) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = "Back button",
                                    modifier = Modifier
                                        .clickable {
                                            onBackPressed()
                                        },
                                    colorFilter = ColorFilter.tint(
                                        Color(
                                            barCodeModel?.properties?.iconProperties?.color
                                                ?: 0XFFFFFF
                                        )
                                    )
                                )
                                AppTextView(
                                    textProperties = contextualBase.titleText,
                                )
                                Image(
                                    painter = painterResource(
                                        id = if (flashStatus.value)
                                            R.drawable.ic_flash_on_vd_white_24
                                        else R.drawable.ic_flash_off_vd_white_24
                                    ),
                                    contentDescription = "Back button",
                                    modifier = Modifier
                                        .clickable {
                                            if (flashStatus.value) {
                                                cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                                            } else {
                                                cameraSource!!.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                                            }
                                            flashStatus.value = flashStatus.value.not()
                                        },
                                    colorFilter = ColorFilter.tint(
                                        Color(
                                            barCodeModel?.properties?.iconProperties?.color
                                                ?: 0XFFFFFF
                                        )
                                    )
                                )
                            }
                            AppTextView(
                                textProperties = contextualBase.contentText,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (!CameraUtils.allPermissionsGranted(this)) {
            CameraUtils.requestRuntimePermissions(this)
        } else {
            intent.extras?.getString(PROPERTY)?.let {
                val contextualBase = Gson().fromJson(it, ContextualBase::class.java)
                val barCodeModel =
                    Gson().fromJson(contextualBase.extraJson, BarCodeModel::class.java)
                        ?: BarCodeModel(
                            properties = BarCodeModel.Properties(
                                width = scannerWidth,
                                height = scannerHeight
                            )
                        )
                this.barCodeModel = barCodeModel
                workflowModel?.markCameraFrozen()
                currentWorkflowState = WorkflowState.NOT_STARTED
                cameraSource?.setFrameProcessor(
                    BarcodeProcessor(
                        barCodeModel,
                        graphicOverlay!!,
                        workflowModel!!
                    )
                )
                workflowModel?.setWorkflowState(WorkflowState.DETECTING)
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        BarcodeResultFragment.dismiss(supportFragmentManager)
    }

    override fun onPause() {
        super.onPause()
        currentWorkflowState = WorkflowState.NOT_STARTED
        stopCameraPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        cameraSource = null
    }

    private fun startCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        val cameraSource = this.cameraSource ?: return
        if (!workflowModel.isCameraLive) {
            try {
                workflowModel.markCameraLive()
                preview?.start(cameraSource)
            } catch (e: IOException) {
                Log.e(TAG, "Failed to start camera preview!", e)
                cameraSource.release()
                this.cameraSource = null
            }
        }
    }

    private fun stopCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        if (workflowModel.isCameraLive) {
            workflowModel.markCameraFrozen()
            cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
            preview?.stop()
        }
    }

    private fun setUpWorkflowModel() {
        workflowModel = ViewModelProviders.of(this).get(WorkflowModel::class.java)

        workflowModel!!.workflowState.observe(this, Observer { workflowState ->
            if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                return@Observer
            }

            currentWorkflowState = workflowState

            val wasPromptChipGone = promptChip?.visibility == View.GONE

            when (workflowState) {
                WorkflowState.DETECTING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_point_at_a_barcode)
                    startCameraPreview()
                }

                WorkflowState.CONFIRMING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_move_camera_closer)
                    startCameraPreview()
                }

                WorkflowState.SEARCHING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_searching)
                    stopCameraPreview()
                }

                WorkflowState.DETECTED, WorkflowState.SEARCHED -> {
                    promptChip?.visibility = View.GONE
                    stopCameraPreview()
                }

                else -> promptChip?.visibility = View.GONE
            }

            val shouldPlayPromptChipEnteringAnimation =
                wasPromptChipGone && promptChip?.visibility == View.VISIBLE
            promptChipAnimator?.let {
                if (shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
            }
        })

        workflowModel?.detectedBarcode?.observe(this, Observer { barcode ->
            if (barcode != null) {

                val delay = if (barCodeModel?.properties?.showResult == true) {
                    val barcodeFieldList = ArrayList<BarcodeField>()
                    barcodeFieldList.add(BarcodeField("Raw Value", barcode.rawValue ?: ""))
                    BarcodeResultFragment.show(supportFragmentManager, barcodeFieldList)
                    3000L
                } else 0L

                barCodeModel?.properties?.tag?.let {
                    Contextual.tagString(it, barcode.rawValue ?: "")
                }

                Handler().postDelayed({
                    val intent = Intent()
                    intent.putExtras(Bundle().apply {
                        putString(BARCODE_DATA, barcode.rawValue)
                    })
                    BarcodeScannerGuideBlock.resultListener?.onResultReceived(Activity.RESULT_OK, intent)
                    finish()
                }, delay)
            }
        })


    }

    override fun onBackPressed()
    {
        BarcodeScannerGuideBlock.resultListener?.onResultReceived(Activity.RESULT_CANCELED,null)
        super.onBackPressed()
    }

    companion object {
        private const val TAG = "BarcodeActivity"
        private const val PROPERTY = "BarcodeProperty"
        const val BARCODE_DATA = "BarcodeData"

        fun newIntent(
            context: Context,
            payload: String,
        ): Intent {
            return Intent(context, BarcodeScanningActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putString(PROPERTY, payload)
                }
                putExtras(bundle)
            }
        }
    }
}
