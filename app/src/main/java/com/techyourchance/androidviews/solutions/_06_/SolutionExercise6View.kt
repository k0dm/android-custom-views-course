package com.techyourchance.androidviews.solutions._06_

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.techyourchance.androidviews.CustomViewScaffold
import com.techyourchance.androidviews.R
import kotlinx.coroutines.NonCancellable.start
import kotlin.math.pow
import kotlin.math.sqrt

class SolutionExercise6View : CustomViewScaffold {

    private val checkmarkPaint = Paint()
    private var checkmarkLineSize = 0f
    private var checkmarkShortSideLength = 0f
    private val pivotPoint = PointF()
    private val referenceCheckmarkPath = Path()
    private val checkmarkPath = Path()
    private var scale = 1f

    private var pathValueAnimator: ValueAnimator? = null
    private var scaleValueAnimator: ValueAnimator? = null
    private var animatorSet: AnimatorSet? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    fun startAnimation(durationMs: Long) {
        pathValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = LinearInterpolator()
            duration = durationMs
            addUpdateListener {
                updateCheckmarkPath(it.animatedValue as Float)
            }
        }
        scaleValueAnimator = ValueAnimator.ofFloat(1f, 1.2f, 1f).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = durationMs / 2
            addUpdateListener {
                scale = it.animatedValue as Float
                invalidate()
            }
        }
        animatorSet = AnimatorSet().apply {
            play(pathValueAnimator)
            play(scaleValueAnimator).after(durationMs * 3/4)
            start()
        }
    }

    fun stopAnimation() {
        animatorSet?.cancel()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        checkmarkLineSize = dpToPx(LINE_SIZE_DP)

        checkmarkPaint.color = ContextCompat.getColor(context, R.color.green)
        checkmarkPaint.style = Paint.Style.STROKE
        checkmarkPaint.strokeWidth = checkmarkLineSize
        checkmarkPaint.isAntiAlias = true

        updateReferenceCheckmarkPath(w, h, checkmarkLineSize)
    }

    private fun updateReferenceCheckmarkPath(viewWidth: Int, viewHeight: Int, minPadding: Float) {
        checkmarkShortSideLength = calculateCheckmarkShortSideLength(viewWidth, viewHeight, minPadding)
        val checkmarkWidth = sqrt(5f) * checkmarkShortSideLength
        val checkmarkHeight = 2 * checkmarkShortSideLength / sqrt(5f)
        val checkmarkTop = (viewHeight - checkmarkHeight) / 2
        val checkmarkLeft = (viewWidth - checkmarkWidth) / 2
        val pivotPointX = checkmarkLeft + sqrt(checkmarkShortSideLength.pow(2) - checkmarkHeight.pow(2))

        val startPoint = PointF(checkmarkLeft, checkmarkTop)
        pivotPoint.set(pivotPointX, checkmarkTop + checkmarkHeight)
        val endPoint = PointF(checkmarkLeft + checkmarkWidth, checkmarkTop)

        referenceCheckmarkPath.reset()
        referenceCheckmarkPath.moveTo(startPoint.x, startPoint.y)
        referenceCheckmarkPath.lineTo(pivotPoint.x, pivotPoint.y)
        referenceCheckmarkPath.lineTo(endPoint.x, endPoint.y)
    }

    private fun updateCheckmarkPath(fraction: Float) {
        val pathMeasure = PathMeasure(referenceCheckmarkPath, false)
        val totalLength = 3 * checkmarkShortSideLength
        checkmarkPath.reset()
        pathMeasure.getSegment(0f, fraction * totalLength, checkmarkPath, true)
        invalidate()
    }

    private fun calculateCheckmarkShortSideLength(viewWidth: Int, viewHeight: Int, minPadding: Float): Float {
        val checkmarkShortSideLengthCandidate = sqrt((viewWidth - 2 * minPadding).pow(2) / 5)
        val checkMarkHeightCandidate = 2 * checkmarkShortSideLengthCandidate / sqrt(5f)
        return if (checkMarkHeightCandidate <= viewHeight - 2 * minPadding) {
            checkmarkShortSideLengthCandidate
        } else {
            (viewHeight - 2 * minPadding) * sqrt(5f) / 2
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.scale(scale, scale, pivotPoint.x, pivotPoint.y)
        canvas.drawPath(checkmarkPath, checkmarkPaint)
    }

    companion object {
        const val LINE_SIZE_DP = 15f
    }
}