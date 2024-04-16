package com.contextu.al.appFieldEdit.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import com.contextu.al.Contextual
import com.contextu.al.ContextualBase
import com.contextu.al.R
import com.contextu.al.Temp
import com.contextu.al.core.removeStrSymbol
import com.contextu.al.getHeight
import com.contextu.al.getWidth
import com.contextu.al.ui.components.ComponentFactory
import com.contextu.al.ui.components.PointziUIComponentFactory
import com.contextu.al.ui.components.components.ButtonView
import com.contextu.al.ui.components.components.ImageView
import java.util.Locale

class AppFieldUiHandler
{



    fun getView(activity: Activity, widget: ContextualBase): View
    {
        try {
            val componentFactory: ComponentFactory = PointziUIComponentFactory(activity)
            val inflater = activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            ) as LayoutInflater

            val relativeView = inflater.inflate(R.layout.relative_layout, null) as ViewGroup

            relativeView.clipToPadding = false
            relativeView.clipChildren = false
            relativeView.clipToOutline = false

            val outView = inflater.inflate(R.layout.ui_list, null) as LinearLayout
            outView.clipToPadding = false

            var width = getWidth(activity, widget.baseView.width)
            var height = getHeight(activity, widget.baseView.height)

            if (widget.baseView.width.equals("100%", true) &&
                widget.baseView.height.equals("100%", true)
            ) {
                val innerMargins = widget.baseView.margin.adaptToScreen(
                        multiplier = 2f
                )

                // innerMargins[0] left
                width -= innerMargins.left
                // top
                height -= innerMargins.top
                // right
                width -= innerMargins.right
                // bottom
                height -= innerMargins.bottom
            }
            val padding = widget.baseView.padding.adaptToScreen()

            val paddedWidth =
                if (width >= (padding.right + padding.left)) {
                    width - (padding.right + padding.left)
                } else {
                    width
                }
            val paddedHeight =
                if (height >= (padding.top + padding.bottom)) {
                    height - (padding.top + padding.bottom)
                } else {
                    height
                }
            relativeView.layoutParams = RelativeLayout.LayoutParams(
                    paddedWidth,
                    paddedHeight
            )
            outView.layoutParams = LinearLayout.LayoutParams(
                    width,
                    height
            )

            val borderWidth = Contextual.state.device.convertDpToPx(
                    widget.baseView.getBorderWidth().toFloat()
            )
            relativeView.setPadding(
                    20,
                    20,
                    20,
                    20
            )

            // Adding Image
            widget.images.forEach { image ->

                val imageCardView: ImageView =
                    inflater.inflate(R.layout.ui_image, null) as ImageView

                relativeView.addView(
                        imageCardView.update(
                                image,
                                activity,
                                width,
                                height,
                                padding
                        )
                )
            }


            val title = widget.titleText
            if (!title.text.isNullOrEmpty()) {
                val titleTV =
                    componentFactory.buildComponent(title, inflater, R.layout.ui_text, null)
                if (outView.parent != null) {
                    (outView.parent as ViewGroup).removeView(outView) // <-
                }
                // fix
                outView.addView(titleTV)
            }

            val content = widget.contentText
            if (!content.text.isNullOrEmpty()) {
                val contentView = componentFactory
                    .buildComponent(content, inflater, R.layout.ui_text, null)
                var tipCornerRadius = widget.baseView.getCornerRadius()
                if (tipCornerRadius > 30) {
                    tipCornerRadius = 50

                    val marginParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            2000.0f
                    )

                    var addCountToMargin = 5

                    for (i in 30 until tipCornerRadius) {
                        if (i % 10 == 0) {
                            marginParams.setMargins(
                                    10,
                                    addCountToMargin,
                                    30,
                                    addCountToMargin
                            )
                            addCountToMargin += 5
                        }
                    }
                    // Set Layout Params for Margins if the Border Radius Changes
                    // contentView.layoutParams = marginParams
                }
                outView.addView(contentView)
            }

            for ((index, button) in widget.buttonList.withIndex()) {
                outView.addView(
                        run {
                            val buttonView = (inflater.inflate(R.layout.ui_button, null) as ButtonView)
                                .build(button)
                                .setActions(
                                        widget.feedID ?: "",
                                        button,
                                        widget.launcher,
                                        sendAccepted = true,
                                        foreverCampaign = widget.deletionConditions
                                )

                            val params = buttonView.layoutParams as RelativeLayout.LayoutParams

                            buttonView.clipToOutline = false

                            val newParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    try {
                                        val lineMultiplier = removeStrSymbol(button.lineHeight ?: "", "%")
                                            .toFloat() / 100
                                        (Contextual.state.device.convertDpToPx(button.fontSize) * lineMultiplier * 1.1)
                                            .toInt() +
                                                Contextual.state.device.convertDpToPx(button.padding.top) +
                                                Contextual.state.device.convertDpToPx(button.padding.bottom)
                                    } catch (e: NumberFormatException) {
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    }
                            )

                            newParams.setMargins(
                                    params.leftMargin,
                                    params.topMargin,
                                    params.rightMargin,
                                    params.bottomMargin
                            )
                            buttonView.layoutParams = newParams
                            buttonView
                        }
                )
                if (index < widget.buttonList.size - 1 &&
                    button.margin.bottom + button.margin.top == 0
                ) {
                    val hairline = inflater.inflate(R.layout.ui_hairline, null)
                    val newParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                    )

                    val margins = button.margin.adaptToScreen()
                    newParams.setMargins(
                            margins.left,
                            0,
                            margins.right,
                            0
                    )

                    outView.addView(hairline, newParams)
                }
            }

            if (height > 0) {
                val spacer = View(activity)
                spacer.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1.0F
                )
                outView.addView(spacer)
            }


            /* Button bar */
            var hasPrev = false
            var hasNext = false
            val prevButtonTitle = widget.buttons.prevButton?.text
            val nextButtonTitle = widget.buttons.nextButton?.text
            if (null != prevButtonTitle) {
                if (prevButtonTitle.isNotEmpty()) {
                    hasPrev = true
                }
            }
            if (null != nextButtonTitle) {
                if (nextButtonTitle.isNotEmpty()) {
                    hasNext = true
                }
            }

            if (hasNext && hasPrev) {
                var buttonBar = inflater.inflate(R.layout.button_layout_horizontal, null)
                if (widget.buttons.buttonLayout.toLowerCase(Locale.ROOT) == "vertical") {
                    buttonBar = inflater.inflate(R.layout.button_layout_vertical, null)
                }
                val prevButton = widget.buttons.prevButton
                val nextButton = widget.buttons.nextButton
                if (!prevButtonTitle.isNullOrEmpty()) {
                    val prevBtn = buttonBar.findViewById<View>(R.id.pz_prevButton) as ButtonView
                    prevButton?.let {
                        prevBtn
                            .build(prevButton)
                    }
                }
                if (!nextButtonTitle.isNullOrEmpty()) {
                    val nextBtn = buttonBar.findViewById<View>(R.id.pz_nextButton) as ButtonView
                    nextButton?.let {
                        nextBtn
                            .build(nextButton)

                    }

                    if (widget.buttons.buttonLayout == "vertical") {
                        (nextBtn.layoutParams as RelativeLayout.LayoutParams)
                            .addRule(RelativeLayout.BELOW, R.id.pz_prevButton)
                    }
                }

                val buttonBarTopLayer =
                    buttonBar.findViewById<RelativeLayout>(R.id.pz_buttontopLayer)
                val buttonParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                buttonBarTopLayer.layoutParams = buttonParams

                outView.addView(buttonBarTopLayer)
            } else if (hasNext || hasPrev) {
                val buttonBar = inflater.inflate(R.layout.button_layout_single, null)
                val buttonBarTopLayer =
                    buttonBar.findViewById<RelativeLayout>(R.id.pz_singleButtonLay)
                buttonBarTopLayer.gravity = Gravity.BOTTOM
                val button = buttonBar.findViewById<ButtonView>(R.id.pz_nextButton)
                if (!widget.buttons.nextButton?.text.isNullOrEmpty()) {
                    button
                        .build(widget.buttons.nextButton!!)
                        .setActions(
                                widget.feedID!!,
                                widget.buttons.nextButton!!,
                                widget.launcher,
                                foreverCampaign = widget.deletionConditions,
                                sendAccepted = false
                        )
                } else if (!widget.buttons.prevButton?.text.isNullOrEmpty()) {
                    button
                        .build(widget.buttons.prevButton!!)
                        .setActions(
                                widget.feedID!!,
                                widget.buttons.prevButton!!,
                                widget.launcher,
                                foreverCampaign = widget.deletionConditions,
                                sendAccepted = false
                        )
                }
                outView.addView(buttonBarTopLayer)
            }

            relativeView.addView(outView)

            if (widget.buttons.hasDnd) {
                relativeView.addView(
                        ContextualBase.addDndButton(
                                activity,
                                widget,
                                inflater
                        )
                )
            }

            return relativeView
        } catch (e: Exception) {
            com.contextu.al.debug.Log.e("player", "Error building view: ", e)
            return RelativeLayout(activity)
        }
    }
    fun isViewVisible(view: View): Boolean
    {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val positionX = Contextual.state.device.convertPxToDp(
                location[0].toFloat()
        )
        val positionY = Contextual.state.device.convertPxToDp(
                location[1].toFloat()+view.measuredHeight
        )
        val size = intArrayOf(
                Contextual.state.device.displaySizeDp.x,
                Contextual.state.device.displaySizeDp.y
        )
        return (positionY >= 0 && positionY <= size[1] && positionX >= 0)
    }
    fun scrollToView(view: View,tipViewHeight:Int, onScrollDone: () -> Unit)
    {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        var positionY = Contextual.state.device.convertPxToDp(location[1].toFloat())
        positionY += view.measuredHeight+tipViewHeight
        var parent = view.parent

        while (parent != null && parent !is ScrollView && parent !is NestedScrollView)
        {
            parent = parent.parent
        }

        if(parent !is ScrollView && parent !is NestedScrollView)
        {
            onScrollDone()
            return
        }

        val scrollView = if (parent is ScrollView) parent else  parent as NestedScrollView
        var scrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
        scrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
            if ((positionY>0 && scrollView.scrollY >= positionY.toInt()) || (positionY<0 && scrollView.scrollY==0))
            {
                onScrollDone() // Remove the listener as it's no longer needed
                scrollView.viewTreeObserver.removeOnScrollChangedListener(scrollChangedListener)
            }
        }

        scrollView.viewTreeObserver.addOnScrollChangedListener(scrollChangedListener)
        scrollView.post {
            if (scrollView is ScrollView) scrollView.smoothScrollTo(
                    0,
                    positionY.toInt()
            )
            if (scrollView is NestedScrollView) scrollView.smoothScrollTo(
                    0,
                    positionY.toInt()
            )
        }

    }

}