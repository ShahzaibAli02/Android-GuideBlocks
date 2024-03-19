package com.contextu.al.circlevideo

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.contextu.al.R
import com.contextu.al.common.extensions.clickedOutside
import com.contextu.al.common.extensions.dismiss
import com.contextu.al.model.customguide.ContextualContainer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.cos

class CircleVideoGuideBlock(private val activity: AppCompatActivity,private val contextualContainer: ContextualContainer): Dialog(activity) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
    }
    private var closed_manually:Boolean=false
    fun show(url: String){
        setContentView(R.layout.circle_video)
        val container:CircularWebViewContainer = findViewById(R.id.container)
        val webView:CircularWebView = findViewById(R.id.webView)
        val btnClose:FloatingActionButton = findViewById(R.id.btnClose)
        //create code to support video inside web-view
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true
        webSettings.setSupportZoom(false)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels * 0.90

        container.layoutParams.width = width.toInt()
        container.layoutParams.height = width.toInt()

        webView.loadUrl(url)
        btnClose.setOnClickListener {
            closed_manually=true
            dismiss()
        }
        show()
    }
    override fun dismiss() {

        if(closed_manually)
            contextualContainer.dismiss()
        else
            contextualContainer.clickedOutside()
        super.dismiss()
    }

}