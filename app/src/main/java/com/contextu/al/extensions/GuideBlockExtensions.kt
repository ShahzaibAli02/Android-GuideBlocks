package com.contextu.al.extensions

import android.view.View
import com.contextu.al.model.customguide.ContextualContainer


fun ContextualContainer.complete(view: View? =null)
{
    this.guidePayload.complete.onClick(null)
    this.guidePayload.nextStep.onClick(null)
}
fun ContextualContainer.clicked(view: View? =null)
{
    this.guidePayload.clickInside.onClick(null)
}
fun ContextualContainer.dismiss(view: View? =null)
{
    this.guidePayload.dismissGuide.onClick(null)
    this.guidePayload.prevStep.onClick(null)
}
