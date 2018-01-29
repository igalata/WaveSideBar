package com.igalata.wavesidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.widget.FrameLayout
import dpToPx

/**
 * Created by irinagalata on 1/26/18.
 */
class WaveSideBar : FrameLayout {

    private var isExpanded = false

    private var startX = 0f
    private var startY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private var previousActionType = 0

    private val offset by lazy { dpToPx(R.dimen.offset) }
    private val pullOffset by lazy { dpToPx(R.dimen.pull_offset) }

    private var paint: Paint? = null
    private var path: Path? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)

        paint = Paint().apply {
            color = ContextCompat.getColor(context, android.R.color.white)
            style = Paint.Style.FILL
        }
        path = Path()
    }

    override fun onDraw(canvas: Canvas?) {

        if (previousActionType == ACTION_DOWN || isExpanded) return
        path?.reset()

        drawBezierCurve()
        canvas?.drawPath(path, paint)
    }

    private fun drawBezierCurve() {
        path?.moveTo(0f, 0f)
        path?.lineTo(0f, height.toFloat())
        path?.cubicTo(
                0f, currentY + 2 * offset,
                currentX, currentY + 2 * offset,
                currentX, currentY)
        path?.cubicTo(
                currentX, currentY - 2 * offset,
                0f, currentY - 2 * offset,
                0f, 0f)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return event.x < offset || isExpanded || super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        currentX = event.x
        currentY = event.y

        var invalidateNeeded = false

        when (event.action) {
            ACTION_DOWN -> {
                startX = event.x
                startY = event.y

                if (event.x >= offset && !isExpanded) return false
            }
            ACTION_MOVE -> {
                invalidateNeeded = startX != currentX
            }
            ACTION_UP -> {
                if (!event.isClick()) {
                    isExpanded = (isExpanded && !event.isPulledBack()) || event.isPulled()
                    if (isExpanded && event.isPulled()) {
                        // todo expand!
                    } else {
                        currentX = 0f
                        currentY = 0f
                    }
                    invalidateNeeded = true
                }
            }
        }

        if (invalidateNeeded) {
            invalidate(0, 0, currentX.toInt(), height)
        }
        previousActionType = event.action
        return true
    }

    private fun MotionEvent.isClick(): Boolean {
        return Math.abs(this.x - startX) < 10 && Math.abs(this.y - startY) < 10
    }

    private fun MotionEvent.isPulled(): Boolean {
        return this.x - startX > pullOffset
    }

    private fun MotionEvent.isPulledBack(): Boolean {
        return startX - this.x > pullOffset
    }
}