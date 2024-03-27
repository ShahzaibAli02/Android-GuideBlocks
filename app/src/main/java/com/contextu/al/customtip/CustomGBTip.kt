package com.contextu.al.customtip

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.contextu.al.Player
import com.contextu.al.R
import com.contextu.al.common.extensions.dismiss
import com.contextu.al.customtip.viewmodels.CustomGbTipViewModel
import com.contextu.al.databinding.LytCustomTipBinding
import com.contextu.al.model.customguide.ContextualContainer
import com.contextu.al.model.ui.Box

class CustomGBTip(val activity: Activity, val contextualContainer: ContextualContainer)
{

    companion object
    {
        val GB_KEY="TestTip"
    }


    private val mViewModel:CustomGbTipViewModel by lazy {
        CustomGbTipViewModel(contextualContainer)
    }
    fun showCustomTip()
    {

        mViewModel.parseJson() //PARSE EXTRA JSON TEXT

        val widget = contextualContainer.guidePayload.guide
        val tipAttachHelper = TipAttachHelper(activity,widget)
        val mAnchor = contextualContainer.operations.findTarget(widget)
        mAnchor?.let { target ->

            val mViewToDisplay=LytCustomTipBinding.inflate(activity.layoutInflater)
            mViewToDisplay.txtMessage.text = widget.contentText.text
            mViewToDisplay.btnDismiss.text= "Dismiss"
            mViewToDisplay.btnDismiss.setOnClickListener {

                mViewModel.onDismiss()
                tipAttachHelper.dismiss()
            }

            mViewToDisplay.cardView.setCardBackgroundColor(widget.baseView.backgroundColor)
            tipAttachHelper.showTooltip(
                    anchor = target,
                    tipContent = mViewToDisplay.root,
                    tipPointerColor = widget.baseView.backgroundColor
            )

        }
    }

}