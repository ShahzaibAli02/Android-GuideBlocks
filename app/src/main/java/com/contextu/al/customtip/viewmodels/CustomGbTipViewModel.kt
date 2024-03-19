package com.contextu.al.customtip.viewmodels

import androidx.lifecycle.ViewModel
import com.contextu.al.common.extensions.dismiss
import com.contextu.al.customtip.CustomGBTip
import com.contextu.al.customtip.model.CustomTipModel
import com.contextu.al.model.customguide.ContextualContainer
import com.google.gson.Gson

class CustomGbTipViewModel(val contextualContainer: ContextualContainer):ViewModel()
{
    var mCustomGBTip: CustomTipModel? = null
    private set

    fun parseJson()
    {
        contextualContainer.guidePayload.guide.extraJson?.let {extraJson->
          runCatching {
              mCustomGBTip=Gson().fromJson(extraJson,CustomTipModel::class.java)
          }
        }

    }

    fun onDismiss()
    {
        contextualContainer.dismiss()
    }
}