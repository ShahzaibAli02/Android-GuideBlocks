package com.contextu.al.common.extensions


import android.view.View
import com.contextu.al.model.customguide.ContextualContainer
import kotlinx.coroutines.flow.firstOrNull


fun ContextualContainer.complete(view: View? =null)
{
    this.guidePayload.complete.onClick(null)
}
fun ContextualContainer.clicked(view: View? =null)
{
    this.guidePayload.clickInside.onClick(null)
}
fun ContextualContainer.clickedOutside(view: View? =null)
{
    this.guidePayload.clickOutside.onClick(null)
}
fun ContextualContainer.clickedInside(view: View? =null)
{
    this.guidePayload.clickInside.onClick(null)
}
fun ContextualContainer.dismiss(view: View? =null)
{
    this.guidePayload.dismissGuide.onClick(view)
}
fun ContextualContainer.previous(view: View? =null)
{
    this.guidePayload.prevStep.onClick(view)
}


fun ContextualContainer.setTag(key:String,tag:Int)
{
    this.tagManager.setNumericTag(key,tag)
}
fun ContextualContainer.setTag(key:String,tag:String)
{
    this.tagManager.setStringTag(key,tag)
}

suspend fun ContextualContainer?.getIntegerTag(key:String,default:Int=-1):Int
{
    return  this?.tagManager?.getTag(key)?.firstOrNull()?.tagIntegerValue?:default
}
suspend fun ContextualContainer?.getStringTag(key:String,default:String=""):String
{
    return  this?.tagManager?.getTag(key)?.firstOrNull()?.tagStringValue?:default
}