package com.contextu.al.barcodescanner

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.contextu.al.R
import com.contextu.al.barcodescanner.camera.CameraSource
import com.contextu.al.barcodescanner.camera.CameraSourcePreview
import com.contextu.al.barcodescanner.camera.GraphicOverlay
import com.contextu.al.barcodescanner.camera.WorkflowModel
import com.contextu.al.barcodescanner.camera.WorkflowModel.*
import com.contextu.al.common.model.BarCodeModel
import com.contextu.al.utils.CameraUtils
import com.google.android.gms.common.internal.Objects
import com.google.android.material.chip.Chip
import java.io.IOException
import java.util.ArrayList

class BarcodeScanningActivity : AppCompatActivity(), OnClickListener {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var settingsButton: View? = null
    private var flashButton: View? = null
    private var promptChip: Chip? = null
    private var promptChipAnimator: AnimatorSet? = null
    private var workflowModel: WorkflowModel? = null
    private var currentWorkflowState: WorkflowState? = null
    private var scannerWidth = 70f
    private var scannerHeight = 80f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_live_barcode)
        preview = findViewById(R.id.camera_preview)
        graphicOverlay = findViewById<GraphicOverlay>(R.id.camera_preview_graphic_overlay).apply {
            setOnClickListener(this@BarcodeScanningActivity)
            cameraSource = CameraSource(this)
        }

        promptChip = findViewById(R.id.bottom_prompt_chip)
        promptChipAnimator =
            (AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter) as AnimatorSet).apply {
                setTarget(promptChip)
            }

        findViewById<View>(R.id.close_button).setOnClickListener(this)
        flashButton = findViewById<View>(R.id.flash_button).apply {
            setOnClickListener(this@BarcodeScanningActivity)
        }
        intent.extras?.getParcelable<BarCodeModel>(PROPERTY)?.let {
            scannerWidth = it.properties.width ?: scannerWidth
            scannerHeight = it.properties.height ?: scannerHeight
            findViewById<TextView>(R.id.title).apply {
                text = it.properties.title
            }
            findViewById<TextView>(R.id.description).apply {
                text = it.properties.description
            }
        }

        setUpWorkflowModel()
    }

    override fun onResume() {
        super.onResume()

        if (!CameraUtils.allPermissionsGranted(this)) {
            CameraUtils.requestRuntimePermissions(this)
        } else {
            intent.extras?.getParcelable<BarCodeModel>(PROPERTY)?.let {
                scannerWidth = it.properties.width ?: scannerWidth
                scannerHeight = it.properties.height ?: scannerHeight
                findViewById<TextView>(R.id.title).apply {
                    text = it.properties.title
                }
                findViewById<TextView>(R.id.description).apply {
                    text = it.properties.description
                }
                workflowModel?.markCameraFrozen()
                settingsButton?.isEnabled = true
                currentWorkflowState = WorkflowState.NOT_STARTED
                cameraSource?.setFrameProcessor(BarcodeProcessor(it, graphicOverlay!!, workflowModel!!))
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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.close_button -> onBackPressed()
            R.id.flash_button -> {
                flashButton?.let {
                    if (it.isSelected) {
                        it.isSelected = false
                        cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                    } else {
                        it.isSelected = true
                        cameraSource!!.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                    }
                }
            }
        }
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
            flashButton?.isSelected = false
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

            val shouldPlayPromptChipEnteringAnimation = wasPromptChipGone && promptChip?.visibility == View.VISIBLE
            promptChipAnimator?.let {
                if (shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
            }
        })

        workflowModel?.detectedBarcode?.observe(this, Observer { barcode ->
            if (barcode != null) {
                val barcodeFieldList = ArrayList<BarcodeField>()
                barcodeFieldList.add(BarcodeField("Raw Value", barcode.rawValue ?: ""))
                BarcodeResultFragment.show(supportFragmentManager, barcodeFieldList)
            }
        })
    }

    companion object {
        private const val TAG = "BarcodeActivity"
        private const val PROPERTY = "BarcodeProperty"

        fun newIntent(
            context: Context,
            barCodeModel: BarCodeModel
        ): Intent {
            return Intent(context, BarcodeScanningActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelable(PROPERTY, barCodeModel)
                }
                putExtras(bundle)
            }
        }
    }
}
