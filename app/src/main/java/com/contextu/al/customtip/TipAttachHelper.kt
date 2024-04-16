package com.contextu.al.customtip

import android.app.Activity
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.contextu.al.Contextual
import com.contextu.al.ContextualBase
import com.contextu.al.CtxObserver
import com.contextu.al.Player
import com.contextu.al.previewMode.PreviewMode
import com.contextu.al.ui.containers.FocusLayer
import com.contextu.al.ui.containers.TipBase
import java.util.Locale

class TipAttachHelper(val activity: Activity, val widget: ContextualBase)
{

    private var drawOverLay:Boolean = true
    fun drawOverLay(value:Boolean):TipAttachHelper
    {
        drawOverLay=value
        return this
    }
    fun dismiss()
    {
        ContextualBase.dismissBaseView()
    }

    fun showTooltip(
        anchor: View,
        tipContent: View,
        tipPointerColor:Int,
        pointerWidth: Int = 20,
        pointerHeight: Int = 20,
        onOutSideDismiss:(()->Unit)?=null,
    )
    {

        if (ContextualBase.mTips.size > 0)
        {
            this.dismiss()
        }

        val resActivity = activity.resources
        val tipPointerSizeHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pointerWidth.toFloat(),
                resActivity.displayMetrics
        ).toInt()

        val tipPointerSizeWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pointerHeight.toFloat(),
                resActivity.displayMetrics
        ).toInt()


        val viewGroup = Player.getActivity()
            .findViewById<View>(android.R.id.content).rootView as ViewGroup



        val position = widget.meta.placement

        val feed = Contextual.state.player.feeds.realMode[widget.feedID]

        if (feed != null)
        {
            feed.actioned = feed.actioned + 1
            Contextual.state.player.feeds.realMode.apply()
        }

        if (!PreviewMode.running)
        {
            CtxObserver.onCreateWidget(
                    widget.feedID,
                    widget.meta.tool.name.toLowerCase(Locale.ROOT)
            )
        }



        if(drawOverLay)
        {
            val focusLayer = FocusLayer(activity)
            focusLayer.setTargetView(anchor)
            focusLayer.pointziWidget = widget
            focusLayer.contentView = tipContent
            focusLayer.activityView = viewGroup
            focusLayer.onOutSideDismiss = onOutSideDismiss
            ContextualBase.mTipsBackground.add((focusLayer as FocusLayer))
            focusLayer.show()
        }


        val tipPopup = TipBase.Builder(activity).parentView(
                    viewGroup
            ).anchorView(anchor)
            .tipPosition(position)
            .contentView(tipContent)
            .pointziWidget(widget)
            .width(tipPointerSizeWidth)
            .height(tipPointerSizeHeight)
            .backgroundColor(tipPointerColor)
            .withAnimation(
                    widget.meta.animation.enter,
                    widget.meta.animation.leave
            ).build()
        ContextualBase.mTips.add(tipPopup as TipBase)
        tipPopup!!.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        tipPopup!!.measure(
                View.MeasureSpec.makeMeasureSpec(
                        0,
                        View.MeasureSpec.UNSPECIFIED
                ),
                View.MeasureSpec.makeMeasureSpec(
                        0,
                        View.MeasureSpec.UNSPECIFIED
                )
        )
        tipPopup.elevation=100f
        tipPopup!!.show()
    }
}