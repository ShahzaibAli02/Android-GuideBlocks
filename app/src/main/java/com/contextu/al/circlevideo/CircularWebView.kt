package com.contextu.al.circlevideo
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.webkit.WebView
import android.widget.FrameLayout
class CircularWebViewContainer : FrameLayout {
    private val clipPath = Path()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        // Enable hardware acceleration to improve performance
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(clipPath)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateClipPath()
    }

    private fun updateClipPath() {
        val radius = Math.min(width, height) / 2f
        val centerX = width / 2f
        val centerY = height / 2f
        clipPath.reset()
        clipPath.addCircle(centerX, centerY, radius, Path.Direction.CW)
        clipPath.close()
    }
}

class CircularWebView : WebView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}