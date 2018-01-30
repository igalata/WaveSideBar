package com.igalata.wavesidebar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import dpToPx
import isClick
import isPulled
import isPulledBack
import java.lang.Math.max

/**
 * Created by irinagalata on 1/26/18.
 */
class WaveSideBar : FrameLayout {

    var expandAnimationDuration = 800L
    var collapseAnimationDuration = 700L

    var view: View? = null
        set(value) {
            field = value
            addView(value)
            value?.visibility = View.GONE
        }

    @ColorRes
    var backgroundColorRes: Int = android.R.color.white

    @DimenRes
    var sideBarWidthRes: Int = R.dimen.side_bar_width

    private var isExpanded = false
    private var animationFinished = false

    private var startX = 0f
    private var startY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private var controlX = 0f

    private var zeroX = 0f
    private var invertedFraction = 1f

    private val offset by lazy { dpToPx(R.dimen.offset) }
    private val pullOffset by lazy { dpToPx(R.dimen.pull_offset) }

    private val sideBarWidth: Float
        get() = dpToPx(sideBarWidthRes)

    private var paint: Paint? = null
    private var shadowPaint: Paint? = null
    private var path: Path? = null
    private var shadowPath: Path? = null
    private var gradient: LinearGradient? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)

        paint = Paint().apply {
            color = ContextCompat.getColor(context, backgroundColorRes)
            style = Paint.Style.FILL
        }
        /*shadowPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.grey)
            style = Paint.Style.FILL
        }*/
        /* gradient = LinearGradient(sideBarWidth, 0f, sideBarWidth + 10, 0f,
                 ContextCompat.getColor(context, R.color.grey),
                 ContextCompat.getColor(context, android.R.color.transparent),
                 android.graphics.Shader.TileMode.CLAMP)*/
        path = Path()
        //shadowPath = Path()
    }

    override fun onDraw(canvas: Canvas?) {
        path?.reset()
        shadowPath?.reset()

        if (isExpanded) {
            drawQuadBezierCurve()
        } else {
            drawCubicBezierCurve()
        }

        canvas?.drawPath(path, paint)
    }

    /* private fun drawShadow() {
         shadowPaint?.shader = gradient
         shadowPaint?.isDither = true

         shadowPath?.moveTo(zeroX, height.toFloat())
         shadowPath?.quadTo(controlX, height / 2f, zeroX, 0f)
         shadowPath?.lineTo(zeroX + 10, 0f)
         shadowPath?.quadTo(controlX + 10, height / 2f, zeroX + 10, height.toFloat())
         shadowPath?.lineTo(zeroX, height.toFloat())
     }
 */
    private fun drawCubicBezierCurve() {
        path?.moveTo(0f, 0f)
        path?.lineTo(0f, height.toFloat())
        path?.lineTo(zeroX, height.toFloat())
        path?.cubicTo(
                zeroX, currentY + 3 * offset,
                zeroX + currentX * invertedFraction, currentY + 3 * offset,
                zeroX + currentX * invertedFraction, currentY)
        path?.cubicTo(
                zeroX + currentX * invertedFraction, currentY - 3 * offset,
                zeroX, currentY - 3 * offset,
                zeroX, 0f)
        path?.lineTo(0f, 0f)
    }

    private fun drawQuadBezierCurve() {
        path?.moveTo(0f, 0f)
        path?.lineTo(0f, height.toFloat())
        path?.lineTo(zeroX, height.toFloat())
        path?.quadTo(controlX, height / 2f, zeroX, 0f)
        path?.lineTo(0f, 0f)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val touchOutside = isExpanded && event.x > sideBarWidth
        val touchEdge = event.x < offset

        return touchEdge || touchOutside || super.onInterceptTouchEvent(event)
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
                controlX = max(currentX, sideBarWidth - offset)
            }
            ACTION_UP -> {
                if (!event.isClick(startX, startY)) {
                    if (!isExpanded && event.isPulled(startX, pullOffset)) {
                        startExpandAnimation()
                    } else if (event.isPulledBack(startX, pullOffset)) {
                        animateCollapsing()
                    } else {
                        animateTension()
                    }
                    invalidateNeeded = true
                } else if (isExpanded) {
                    animateCollapsing()
                }
            }
        }

        if (invalidateNeeded) {
            invalidate(0, 0, currentX.toInt(), height)
        }
        return true
    }

    private fun startExpandAnimation() {
        ValueAnimator.ofFloat(0f, sideBarWidth).apply {
            duration = expandAnimationDuration / 2
            addUpdateListener {
                zeroX = animatedValue as Float
                invertedFraction = 1 - animatedFraction
                invalidate(0, 0, sideBarWidth.toInt(), height)
            }
            addListener(object : OnAnimationFinishedListener {
                override fun onAnimationEnd(animation: Animator?) {
                    finishExpandAnimation()
                    isExpanded = true
                }
            })
        }.start()
        showContent()
    }

    private fun finishExpandAnimation() {
        ValueAnimator.ofFloat(currentX, sideBarWidth).apply {
            duration = expandAnimationDuration / 2 + 200
            interpolator = SpringInterpolator()
            addUpdateListener {
                controlX = animatedValue as Float
                invalidate(0, 0, sideBarWidth.toInt(), height)
            }
            addListener(object : OnAnimationFinishedListener {
                override fun onAnimationEnd(animation: Animator?) {
                    animationFinished = true
                }
            })
        }.start()
    }

    private fun showContent() {
        ViewAnimationUtils.createCircularReveal(
                view, 0, height / 2, 0f, height.toFloat())
                .apply {
                    duration = expandAnimationDuration
                    view?.visibility = View.VISIBLE
                }.start()
    }

    private fun animateCollapsing() {
        animationFinished = false
        hideContent()
        ValueAnimator.ofFloat(sideBarWidth, 0f).apply {
            duration = collapseAnimationDuration
            addUpdateListener {
                zeroX = animatedValue as Float
            }
        }.start()
        ValueAnimator.ofFloat(sideBarWidth, 0f).apply {
            duration = collapseAnimationDuration + 100
            interpolator = BounceInterpolator()
            addUpdateListener {
                controlX = animatedValue as Float
                invalidate(0, 0, sideBarWidth.toInt(), height)
            }
            addListener(object : OnAnimationFinishedListener {
                override fun onAnimationEnd(animation: Animator?) {
                    isExpanded = false
                    clearData()
                }
            })
        }.start()
    }

    private fun animateTension() {
        ValueAnimator.ofFloat(controlX, sideBarWidth).apply {
            duration = collapseAnimationDuration / 2
            interpolator = SpringInterpolator()
            addUpdateListener {
                controlX = animatedValue as Float
                invalidate(0, 0, sideBarWidth.toInt(), height)
            }
        }.start()
    }

    private fun hideContent() {
        ViewAnimationUtils.createCircularReveal(
                view, 0, height / 2, height.toFloat(), 0f)
                .apply {
                    duration = collapseAnimationDuration / 4
                    view?.visibility = View.VISIBLE
                    addListener(object : OnAnimationFinishedListener {
                        override fun onAnimationEnd(animation: Animator?) {
                            view?.visibility = View.GONE
                        }
                    })
                }.start()
    }

    private fun clearData() {
        zeroX = 0f
        invertedFraction = 1f
        controlX = 0f
        currentX = 0f
        currentY = 0f
    }

}