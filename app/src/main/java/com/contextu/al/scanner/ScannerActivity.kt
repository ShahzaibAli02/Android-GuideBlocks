package com.contextu.al.scanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class ScannerActivity: ComponentActivity() {

    companion object {
        const val BARCODE_DATA = "barcode_data"
        fun newIntent(context: Context) = Intent(context, ScannerActivity::class.java)
    }

    private var gmsBarcodeScannerOptions: GmsBarcodeScannerOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initScanner()
    }

    private fun initScanner() {
        gmsBarcodeScannerOptions = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_CODABAR
            )
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(this, gmsBarcodeScannerOptions!!)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val intent = Intent()
                intent.putExtra(BARCODE_DATA, barcode.displayValue)
                setResult(RESULT_OK, intent)
                finish()
            }
            .addOnCanceledListener {

            }
            .addOnFailureListener { e ->

            }
    }
}