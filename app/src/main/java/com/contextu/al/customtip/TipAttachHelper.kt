package com.contextu.al.customtip

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import com.contextu.al.Contextual
import com.contextu.al.ContextualBase
import com.contextu.al.CtxObserver
import com.contextu.al.Player
import com.contextu.al.R
import com.contextu.al.previewMode.PreviewMode
import com.contextu.al.setShapeDrawable
import com.contextu.al.ui.containers.FocusLayer
import com.contextu.al.ui.containers.TipBase
import java.util.Locale

class TipAttachHelper(val activity: Activity, val widget: ContextualBase)
{

    fun setTipBackground(baseView: View, widget: ContextualBase): FrameLayout
    {
        val layoutInflate = activity!!.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater
        val borderOverlap = layoutInflate.inflate(
                R.layout.border_overlap,
                LinearLayout(activity)
        )
        val borderFrame = borderOverlap.findViewById<FrameLayout>(R.id.pz_borderframe)

        val borderRadius = widget.baseView.getCornerRadius()
        var borderWidth = widget.baseView.getBorderWidth()

        borderWidth = Contextual.state.device.convertDpToPx(borderWidth)

        val drawableShape = setShapeDrawable(
                widget.baseView.backgroundColor,
                widget.baseView.borderColor,
                borderRadius,
                borderWidth,
                activity
        )
        borderFrame.background = drawableShape
        borderFrame.setPadding(
                borderWidth,
                borderWidth,
                borderWidth,
                borderWidth
        )
        borderFrame.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (borderFrame.parent != null)
        {
            (borderFrame.parent as ViewGroup).removeView(borderFrame)
        }
        borderFrame.addView(baseView)

        return borderFrame
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
    )
    {

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

        if (ContextualBase.mTips.size > 0)
        {
            return
        }


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

        val focusLayer = FocusLayer(activity)
        focusLayer.setTargetView(anchor)
        focusLayer.pointziWidget = widget
        focusLayer.contentView = tipContent
        focusLayer.activityView = viewGroup
        ContextualBase.mTipsBackground.add((focusLayer as FocusLayer))
        focusLayer.show()


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
        tipPopup!!.show()
    }
}