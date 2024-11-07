package com.techyourchance.androidviews.exercises._05_

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.animation.doOnEnd
import com.techyourchance.androidviews.CustomViewScaffold

class MyCheckmarkView : CustomViewScaffold {

    private val checkPath = Path()
    private val animatedCheckPath = Path()
    private val checkPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(LINE_SIZE_DP)
    }

    private lateinit var pathAnimator: ValueAnimator
    private lateinit var scaleAnimator: ValueAnimator
    private var scaleFactor = 1f
    private lateinit var animatorSet: AnimatorSet

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    fun startAnimation(durationMs: Long) {
        pathAnimator= ValueAnimator.ofFloat(0f, 1f).apply {
            duration = durationMs
            addUpdateListener {
                updateCheckPath(it.animatedValue as Float)
            }
        }


        scaleAnimator = ValueAnimator.ofFloat(1f, 1.2f,1f).apply {
            duration = 500
            addUpdateListener {
                scaleFactor = it.animatedValue as Float
                invalidate()
            }
        }
        animatorSet = AnimatorSet()
        animatorSet.play(pathAnimator).before(scaleAnimator)
        animatorSet.start()
    }

    fun stopAnimation() {
        animatorSet.cancel()
    }

    private fun updateCheckPath(fraction: Float) {
        val pathMeasure = PathMeasure(checkPath, false)
        animatedCheckPath.reset()
        pathMeasure.getSegment(0f, fraction * pathMeasure.length, animatedCheckPath,true)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = dpToPx(LINE_SIZE_DP) / 2
        checkPath.moveTo(padding, h * 0.6f)
        checkPath.lineTo(w * 0.4f, h.toFloat() - padding)
        checkPath.lineTo(w.toFloat() - padding, 0f + padding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.scale(scaleFactor, scaleFactor, width.toFloat()/2, height.toFloat()/2) // Scaling around the center
        canvas.drawPath(animatedCheckPath, checkPaint)
    }

    companion object {
        const val LINE_SIZE_DP = 15f
    }
}