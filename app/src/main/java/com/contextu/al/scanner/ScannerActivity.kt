package com.contextu.al.scanner

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class ScannerActivity: ComponentActivity() {

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
                Log.e("--------->", "-------->"+barcode)
            }
            .addOnCanceledListener {

            }
            .addOnFailureListener { e ->

            }
    }
}