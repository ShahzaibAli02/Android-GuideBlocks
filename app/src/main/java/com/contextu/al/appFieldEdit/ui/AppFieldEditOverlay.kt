package com.contextu.al.appFieldEdit.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Region
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.widget.addTextChangedListener
import com.contextu.al.Player
import com.contextu.al.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AppFieldEditOverlay(context: Context, attrs: AttributeSet?=null) : ViewGroup(context, attrs) {

    private var excludedView: View? = null
    private val overlayPaint: Paint = Paint().apply {
        color = Color.parseColor("#99000000") // Black with 50% opacity
        elevation=1f
    }

    private var rectToExclude : Rect  = Rect()
    private var mCloseImageView: ImageView ? =null
    private var mButton: FloatingActionButton? =null
    private var mCloseButton: ImageView? =null
    private var mTextView: TextView ? =null
    private var mHeight:Int=0
    private var mWidth:Int=0
    private var onClose:( ()->Unit ) ? = null
    private var isClosed:Boolean=false

    fun setOnCloseListener(onClose:()->Unit): AppFieldEditOverlay
    {
        this.onClose=onClose
        return this
    }

    fun drawTextView(message:String): AppFieldEditOverlay
    {
        mTextView = TextView(context)
        mTextView?.setTextColor(Color.WHITE)
        mTextView?.text = message
        mTextView?.gravity= Gravity.CENTER
        mTextView?.setPadding(5,10,5,10)
        mTextView?.setBackgroundResource(R.drawable.rounded_back)
        val layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        layoutParams.leftMargin = 0  // Set the x coordinate
        layoutParams.topMargin = 0   // Set the y coordinate
        mTextView?.layoutParams = layoutParams
        addView(mTextView)
        return this
    }

    fun drawDoneButton(): AppFieldEditOverlay
    {
        mButton = FloatingActionButton(context)
        mButton?.setImageResource(R.drawable.baseline_check_24)
        mButton?.elevation = 100f
        mButton?.size=FloatingActionButton.SIZE_MINI
        val layoutParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        mButton?.layoutParams = layoutParams
        addView(mButton)
        return this
    }

    fun drawCloseButton(): AppFieldEditOverlay
    {
        mCloseButton = ImageView(context)
        mCloseButton?.setImageResource(com.contextu.al.R.drawable.ic_close)
        mCloseButton?.elevation = 100f
        mCloseButton?.setBackgroundResource(R.drawable.selectable_background)
        mCloseButton?.setOnClickListener {
            isClosed = true
            onClose?.invoke()
        }

        val layoutParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        mCloseButton?.layoutParams = layoutParams
        addView(mCloseButton)
        return this
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // The view's size has changed, you can access the height and width here
        mHeight=h
        mWidth=w

        updateSizes()
    }


    fun canDrawOnBottom(topY:Int,bottomY:Int,viewToDraw:View):Boolean
    {
        val bottomHeight=mHeight-bottomY
        return bottomHeight>viewToDraw.measuredHeight
    }

    fun setExcludedView(view: View): AppFieldEditOverlay
    {
        excludedView = view
        val editText=EditText(context)
        editText.addTextChangedListener {  }
        excludedView?.getGlobalVisibleRect(rectToExclude)
//        requestLayout()
//        invalidate()
        return this
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            // Check if touch event is within the excluded area
            if (rectToExclude?.contains(event.x.toInt(), event.y.toInt())==false) {
                // Touch event is outside the excluded area, consume the event
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun show(): AppFieldEditOverlay
    {
        val rootView = Player.getActivity()
            .findViewById<View>(android.R.id.content)
            .rootView as ViewGroup

        rootView.addView(
                this,
                FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        )
//        requestLayout()
//        this.invalidate()
        return this
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int)
    {
        val childCount = childCount
        var left = 0
        var top = 0
        var right = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)

            left=child.marginLeft
            top=child.marginTop
            right=child.marginRight
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            child.layout(left,top, (left + childWidth)-right, top + childHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        measureChild(addButton, widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)

        measureChildren(childWidthMeasureSpec, childHeightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun dispatchDraw(canvas: Canvas)
    {
        rectToExclude?.let {
            canvas.clipRect(it.left.toFloat(), it.top.toFloat(), it.right.toFloat(), it.bottom.toFloat(), Region.Op.DIFFERENCE)
        }

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)
        super.dispatchDraw(canvas)

    }


    private fun updateSizes()
    {

        mCloseImageView?.let {
            (it.layoutParams as FrameLayout.LayoutParams).let {
                it.leftMargin=(mWidth-mCloseImageView!!.measuredWidth)-20
                mCloseImageView!!.layoutParams=it

            }
        }


        mTextView?.let {
            val horizontalMargin=20
            val verticalMargin=20
            val topMargin= if(canDrawOnBottom(rectToExclude.top,rectToExclude.bottom,mTextView!!)) rectToExclude.bottom+verticalMargin else rectToExclude.top-verticalMargin

            (it.layoutParams as FrameLayout.LayoutParams).let {

                it.topMargin= topMargin
                it.leftMargin= (mWidth/2)-(mTextView!!.measuredWidth/2)+horizontalMargin
                it.rightMargin= horizontalMargin
                mTextView!!.layoutParams=it

            }
        }

        mButton?.let {

            (it.layoutParams as FrameLayout.LayoutParams).let {
                it.topMargin= mHeight-mButton!!.measuredHeight-100
                it.leftMargin= mWidth-mButton!!.measuredWidth-60
                mButton!!.layoutParams=it

            }
        }

        mCloseButton?.let {

            (it.layoutParams as FrameLayout.LayoutParams).let {

                it.topMargin= 30
                it.leftMargin= (mWidth-mCloseButton!!.measuredWidth)-20

                mCloseButton!!.layoutParams=it

            }
        }

    }
}
