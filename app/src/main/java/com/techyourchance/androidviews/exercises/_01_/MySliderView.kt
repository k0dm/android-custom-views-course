package com.techyourchance.androidviews.exercises._01_

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.techyourchance.androidviews.CustomViewScaffold
import com.techyourchance.androidviews.R

class MySliderView : CustomViewScaffold {

    private var lineXLeft: Float = 0f
    private var lineXRight: Float = 0f
    private var lineYPos: Float = 0f
    private var lineHeight: Float = 0f

    private var circleXCenter: Float = 0f
    private var circleYCenter: Float = 0f
    private var circleRadius: Float = 0f

    private val paint = Paint()

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        circleRadius = dpToPx(10)
        lineYPos = h.toFloat() / 2

        val paddingHorizontal = dpToPx(10)
        lineHeight = h - paddingHorizontal * 2
        lineXLeft = paddingHorizontal + circleRadius
        lineXRight = w - paddingHorizontal - circleRadius

        circleXCenter = w.toFloat() / 2
        circleYCenter = lineYPos
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.GRAY
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE

        canvas.drawLine(lineXLeft, lineYPos, lineXRight, lineYPos, paint)

        paint.color = ContextCompat.getColor(context, R.color.primary)
        paint.style = Paint.Style.FILL

        canvas.drawCircle(circleXCenter, circleYCenter, circleRadius, paint)
    }
}